package ru.rgs.openshift.test.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import com.splunk.logging.HttpEventCollectorLogbackAppender

import java.nio.charset.Charset

/**
 *
 *
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 * (С) RGS Group, http://www.rgs.ru
 * Created on 2016-06-07
 */

abstract class LogbackBaseInit extends Script {

    def allowedProtocols = ["http", "https"]

    def splunkAppenders = []

    def splunkAppenders(map) { this.splunkAppenders = map }

    def attachSplunkAppenders = {}

    def attachSplunkAppenders(closure) { this.attachSplunkAppenders = closure }

    def fileAppenders = []

    def fileAppenders(map) { this.fileAppenders = map }

    def attachFileAppenders = {}

    def attachFileAppenders(closure) { this.attachFileAppenders = closure }

    def run() {

        initPropertiesCommon()
        initPropertiesSplunk()
        initPropertiesFile()
        initPropertiesAdditional()

        println "\n===== Logging properies =====\n"
        binding.getVariables().sort().each { println "$it.key = [$it.value]" }
        println "\n===== End of logging properies =====\n"

        setupSplunk()
        setupFile()
        setupJMX()
    }

    Object setupJMX() {
        jmxConfigurator('ru.rgs.equifaxCreditScore:type=LoggerManager')
    }

    def initPropertiesCommon() {
        // default log level
        def defaultLogLevelStr = "INFO";
        def logLevelStr = System.getProperty("logging.level", defaultLogLevelStr)

        // if by mistake system property "log.level" equals to empty string, Level.valueOf() will return DEBUG level
        // so this is a workaround
        if (logLevelStr.trim().isEmpty()) {
            logLevelStr = defaultLogLevelStr
        }
        setProperty("logLevel", Level.valueOf(logLevelStr))

        // index names
        setProperty("logsIndex", System.getProperty("logging.tech.index.name", "equifaxCreditScore"))
        setProperty("businessLogsIndex", System.getProperty("logging.business.index.name", "businessoperations"))
        setProperty("slowQueryLogsIndex", System.getProperty("logging.slowquery.index.name", "equifaxCreditScore-slowquery"))

        setProperty("node", System.getProperty("logging.node", "undefined"))
        setProperty("environment", System.getProperty("logging.environment", "undefined"))
        setProperty("sparseLogging", System.getProperty("logging.sparse", "true").toBoolean())
    }

    def initPropertiesFile() {
        setProperty("enableLogToFile", System.getProperty("logging.enable.log.to.file", "true").toBoolean())
        setProperty("logPath", System.getProperty("logging.directory", "logs"))
    }

    def initPropertiesSplunk() {
        setProperty("enableLogToSplunk", System.getProperty("logging.enable.log.to.splunk", "true").toBoolean())
        if (enableLogToSplunk.toBoolean()) {
            def splunkProtocol = System.getProperty("logging.splunk.protocol")
            if (!(splunkProtocol in allowedProtocols)) {
                println "Splunk protocol was not not in allowed list of protocols(actual value was [$splunkProtocol], " +
                        "allowed protocols are $allowedProtocols, falling back to default value..."
                splunkProtocol = "http"
            }
            setProperty("splunkProtocol", splunkProtocol)
            setProperty("splunkHost", System.getProperty("logging.splunk.host", "127.0.0.1"))
            def splunkPort = System.getProperty("logging.splunk.port")
            if (splunkPort != null && splunkPort.isInteger()) {
                splunkPort = splunkPort.toInteger()
            } else {
                println "Splunk port was not defined or couldn't be parsed as integer value (actual value was [$splunkPort]), falling back to default value..."
                splunkPort = 8088
            }
            setProperty("splunkPort", splunkPort)
            setProperty("splunkUrl", "$splunkProtocol://$splunkHost:$splunkPort")
            setProperty("splunkToken", System.getProperty("logging.splunk.token", "faketoken"))
        }
    }

    abstract def initPropertiesAdditional()

    def setupFile() {
        if (enableLogToFile.toBoolean()) {
            fileAppenders.each { fileAppender(it.key, it.value['filename'], it.value['pattern']) }
            attachFileAppenders()
        }
    }

    def setupSplunk() {
        if (enableLogToSplunk.toBoolean()) {
            splunkAppenders.each { splunkAppender(it.key, it.value) }
            attachSplunkAppenders()
        }
    }

    def fileAppender = { name, filename, _pattern ->
        // binding is not propagated to inner scope so we must copy the variables
        def _logPath = logPath
        println "Creating File Appender with name = $name, filename = $filename, " +
                "pattern = $_pattern, logPath = $_logPath"
        appender(name, RollingFileAppender) {
            file = "$_logPath/$filename"
            rollingPolicy(FixedWindowRollingPolicy) {
                fileNamePattern = "$_logPath/$filename.%i.zip"
                minIndex = 1
                maxIndex = 5
            }
            triggeringPolicy(SizeBasedTriggeringPolicy) {
                maxFileSize = "64MB"
            }
            encoder(PatternLayoutEncoder) {
                charset = Charset.forName("UTF-8")
                pattern = _pattern
            }
        }
    }

    def splunkAppender = { name, _index ->
        // binding is not propagated to inner scope so we must copy the variables
        def _splunkUrl = splunkUrl
        def _splunkToken = splunkToken
        def _node = node
        def _environment = environment
        def _sparseLogging = sparseLogging
        println "Creating Splunk Appender with name = $name, index = $_index, " +
                "splunkUrl = $_splunkUrl, splunkToken = $_splunkToken, node = $_node"
        appender(name, HttpEventCollectorLogbackAppender) {
            url = _splunkUrl
            token = _splunkToken
            sourcetype = "json-escaped"
            index = _index
            layout(JsonLayout) {
                includeContextName = false
                node = _node
                environment = _environment
                sparseLogging = _sparseLogging
            }
        }
    }
}