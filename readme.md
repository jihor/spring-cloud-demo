#### 1. Build project's docker images:
./gradlew clean docker

####  2. Run Splunk:
docker run -d --name splunk-test -e "SPLUNK_START_ARGS=--accept-license" -e "SPLUNK_USER=root" -p "8000:8000" splunk/splunk
+ create indexes: acme, acme-slowquery, businessoperations
+ create a test token with permissions to write events to these indexes

#### 3. Run Redis:
docker run -d --name redis-test -v /home/jihor/workitems/ppkiv-172_-_Credit_bureau_scoring_service/redis/storage_data:/data redis redis-server --appendonly yes

#### 4. Run Consul:
docker run -d --name=consul-test -p 8500:8500 -h consul docstore.rgs.ru:5000/consul:0.6.4 -server -bootstrap

#### 5. Run backends (ip addresses can to be obtained using 'docker inspect' command):

docker run -d -p 8090:8080 --name backend-a -e "SPRING_APPLICATION_NAME=backend-service-a" -e "SPRING_CLOUD_CONSUL_HOST=172.17.0.2" -e "LOGGING_BUSINESS_INDEX_NAME=businessoperations" -e "LOGGING_DIRECTORY=logs" -e "LOGGING_ENABLE_LOG_TO_FILE=true" -e "LOGGING_ENABLE_LOG_TO_SPLUNK=true" -e "LOGGING_ENVIRONMENT=poc" -e "LOGGING_LEVEL=INFO" -e "LOGGING_NODE=someNiceNode" -e "LOGGING_SLOWQUERY_INDEX_NAME=acme-slowquery" -e "LOGGING_SPARSE=true" -e "LOGGING_SPLUNK_HOST=172.17.0.4" -e "LOGGING_SPLUNK_PORT=8088" -e "SERVER_PORT=8080" -e "LOGGING_SPLUNK_TOKEN=B2F5CEF8-4860-4362-A60F-285AFD42BCE2" -e "LOGGING_TECH_INDEX_NAME=acme" -v $PWD/logs-backend-a:/logs docstore.rgs.ru:5000/openshift-test-backend-service:latest

docker run -d -p 8095:8080 --name backend-b -e "SPRING_APPLICATION_NAME=backend-service-b" -e "SPRING_CLOUD_CONSUL_HOST=172.17.0.2" -e "LOGGING_BUSINESS_INDEX_NAME=businessoperations" -e "LOGGING_DIRECTORY=logs" -e "LOGGING_ENABLE_LOG_TO_FILE=true" -e "LOGGING_ENABLE_LOG_TO_SPLUNK=true" -e "LOGGING_ENVIRONMENT=poc" -e "LOGGING_LEVEL=INFO" -e "LOGGING_NODE=someNiceNode" -e "LOGGING_SLOWQUERY_INDEX_NAME=acme-slowquery" -e "LOGGING_SPARSE=true" -e "LOGGING_SPLUNK_HOST=172.17.0.4" -e "LOGGING_SPLUNK_PORT=8088" -e "SERVER_PORT=8080" -e "LOGGING_SPLUNK_TOKEN=B2F5CEF8-4860-4362-A60F-285AFD42BCE2" -e "LOGGING_TECH_INDEX_NAME=acme" -v $PWD/logs-backend-b:/logs docstore.rgs.ru:5000/openshift-test-backend-service:latest

#### 6. Run frontend (ip addresses can to be obtained using 'docker inspect' command):

docker run -d -p 8080:8080 --name frontend -e "SPRING_APPLICATION_NAME=fontend-service" -e "SPRING_CLOUD_CONSUL_HOST=172.17.0.5" -e "CACHING_REDIS_HOST=172.17.0.4" -e "CACHING_REDIS_PORT=6379" -e "LOGGING_BUSINESS_INDEX_NAME=businessoperations" -e "LOGGING_DIRECTORY=logs" -e "LOGGING_ENABLE_LOG_TO_FILE=true" -e "LOGGING_ENABLE_LOG_TO_SPLUNK=true" -e "LOGGING_ENVIRONMENT=poc" -e "LOGGING_LEVEL=INFO" -e "LOGGING_NODE=someNiceNode" -e "LOGGING_SLOWQUERY_INDEX_NAME=acme-slowquery" -e "LOGGING_SPARSE=true" -e "LOGGING_SPLUNK_HOST=172.17.0.4" -e "LOGGING_SPLUNK_PORT=8088" -e "LOGGING_SPLUNK_TOKEN=B2F5CEF8-4860-4362-A60F-285AFD42BCE2" -e "LOGGING_TECH_INDEX_NAME=acme" -e "RIBBON_HTTP_CLIENT_ENABLED=true" -v $PWD/logs-frontend:/logs docstore.rgs.ru:5000/openshift-test-frontend-service:latest