import ch.qos.logback.classic.Level
import groovy.transform.BaseScript
import ru.rgs.openshift.test.logging.LogbackBaseInit

enum Loggers {
    businessOperationLogger, slowQueryLogging
}

enum Appenders {
    SplunkTechAppender, SplunkBusinessAppender, SplunkSlowqueryAppender,
    FileTechAppender, FileBusinessAppender, FileSlowqueryAppender
}

def init = {
    @BaseScript LogbackBaseInit logbackBaseInitScript
    splunkAppenders(["$Appenders.SplunkTechAppender"     : logsIndex,
                     "$Appenders.SplunkBusinessAppender" : businessLogsIndex,
                     "$Appenders.SplunkSlowqueryAppender": slowQueryLogsIndex])
    fileAppenders(["$Appenders.FileTechAppender"     : [filename: "backend-service.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n"],
                   "$Appenders.FileBusinessAppender" : [filename: "business.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} CorrelationId: %X{CorrelationId} DocumentKey: %X{DocumentKey} Action: %X{Action} System: %X{System} - %msg%n"],
                   "$Appenders.FileSlowqueryAppender": [filename: "slowquery.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} CorrelationId: %X{CorrelationId} DocumentKey: %X{DocumentKey} Action: %X{Action} System: %X{System} Duration: %X{Duration} Method: %X{Method} - %msg%n"]])

    attachSplunkAppenders({
        root(logLevel, ["$Appenders.SplunkTechAppender"])

        // prevent deadlocking and OOM errors on levels finer or equal to DEBUG with Splunk
        logger("org.apache.http", Level.INFO, null, true)
        // if DEBUG or finer logging must be enabled on org.apache.http package, it must be either
        // done to file with additivity=false,
        // or Splunk logging must be turned off at all during that period (enable.log.to.splunk -> false)

        logger("$Loggers.businessOperationLogger", Level.INFO, ["$Appenders.SplunkBusinessAppender"], false)
        logger("$Loggers.slowQueryLogging", Level.INFO, ["$Appenders.SplunkSlowqueryAppender"], false)
    })

    attachFileAppenders({
        root(logLevel, ["$Appenders.FileTechAppender"]);
        logger("$Loggers.businessOperationLogger", Level.INFO, ["$Appenders.FileBusinessAppender"], false)
        logger("$Loggers.slowQueryLogging", Level.INFO, ["$Appenders.FileSlowqueryAppender"], false)
    })

}

init()