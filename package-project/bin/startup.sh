#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$DEMO_HOME" ] && DEMO_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

AGENT_DIR=$DEMO_HOME/skywalking-agent
COLLECTOR_SERVER_LIST=localhost:18080
KAFKA_VERSION=2.11-2.3.0
REPORTER_SERVER_HOST=localhost
REPORTER_SERVER_PORT=11800
KAFKA_SERVER_HOST=localhost:9092

_RUNJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _RUNJAVA=java

if [ ! -d "$DEMO_HOME/kafka_${KAFKA_VERSION}" ]; then
    tar -xvf $DEMO_HOME/kafka_${KAFKA_VERSION}.tgz -C $DEMO_HOME
fi

if [ ! -f "$DEMO_HOME/logs" ]; then
    mkdir $DEMO_HOME/logs
fi

echo "kill kafka service"
ps -ef | grep kafka_${KAFKA_VERSION} | awk '{print $2}' | xargs kill -9
echo "kill projectA service"
ps -ef | grep projectA | awk '{print $2}' | xargs kill -9
echo "kill projectB service"
ps -ef | grep projectB | awk '{print $2}' | xargs kill -9
echo "kill projectC service"
ps -ef | grep projectC | awk '{print $2}' | xargs kill -9
echo "kill projectD service"
ps -ef | grep projectD | awk '{print $2}' | xargs kill -9

#echo "start kafka service"
sh $DEMO_HOME/kafka_${KAFKA_VERSION}/bin/zookeeper-server-start.sh $DEMO_HOME/kafka_${KAFKA_VERSION}/config/zookeeper.properties 2>/dev/null 1> /dev/null &
sleep 20
sh $DEMO_HOME/kafka_${KAFKA_VERSION}/bin/kafka-server-start.sh $DEMO_HOME/kafka_${KAFKA_VERSION}/config/server.properties 2>/dev/null 1> /dev/null &

if [ $? -eq 0 ]; then
  sleep 1
	echo "kafka service started successfully!"
else
	echo "kafka service started failure!"
	exit 1
fi

echo "start project B service"
if [ -f "${AGENT_DIR}/skywalking-agent.jar" ]; then
     PROJECTB_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=business-zone::projectB -Dskywalking.logging.level=warn -Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=${REPORTER_SERVER_HOST} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=${REPORTER_SERVER_PORT} -Dskywalking.plugin.toolkit.log.grpc.reporter.max_message_size=10485760 -Dskywalking.plugin.toolkit.log.grpc.reporter.upstream_timeout=30"
fi
echo "$_RUNJAVA ${PROJECTB_OPTS} -jar $DEMO_HOME/projectB/projectB.jar 2>/dev/null 1> /dev/null &"
$_RUNJAVA ${PROJECTB_OPTS} -jar $DEMO_HOME/projectB/projectB.jar 2>/dev/null 1> /dev/null &

if [ $? -eq 0 ]; then
    sleep 1
	echo "project B service started successfully!"
else
	echo "project B service started failure!"
	exit 1
fi

echo "start project C service"
if [ -f "${AGENT_DIR}/skywalking-agent.jar" ]; then
    PROJECTC_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=business-zone::projectC -Dbootstrap.servers=${KAFKA_SERVER_HOST} -Dskywalking.logging.level=warn -Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=${REPORTER_SERVER_HOST} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=${REPORTER_SERVER_PORT} -Dskywalking.plugin.toolkit.log.grpc.reporter.max_message_size=10485760 -Dskywalking.plugin.toolkit.log.grpc.reporter.upstream_timeout=30"
fi
$_RUNJAVA ${PROJECTC_OPTS} -jar $DEMO_HOME/projectC/projectC.jar 2>/dev/null 1> /dev/null &

if [ $? -eq 0 ]; then
    sleep 1
	echo "project C service started successfully!"
else
	echo "project C service started failure!"
	exit 1
fi

echo "start project D service"
if [ -f "${AGENT_DIR}/skywalking-agent.jar" ]; then
    PROJECTD_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=business-zone::projectD -Dskywalking.logging.level=warn -Dkafka.server=${KAFKA_SERVER_HOST} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=${REPORTER_SERVER_HOST} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=${REPORTER_SERVER_PORT} -Dskywalking.plugin.toolkit.log.grpc.reporter.max_message_size=10485760 -Dskywalking.plugin.toolkit.log.grpc.reporter.upstream_timeout=30"
fi
$_RUNJAVA ${PROJECTD_OPTS} -jar $DEMO_HOME/projectD/projectD.jar 2>/dev/null 1> /dev/null &

if [ $? -eq 0 ]; then
    sleep 1
	echo "project D service started successfully!"
else
	echo "project D service started failure!"
	exit 1
fi

echo "start project A service"
if [ -f "${AGENT_DIR}/skywalking-agent.jar" ]; then
    PROJECTA_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=business-zone::projectA -Dskywalking.logging.level=warn -Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=${REPORTER_SERVER_HOST} -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=${REPORTER_SERVER_PORT} -Dskywalking.plugin.toolkit.log.grpc.reporter.max_message_size=10485760 -Dskywalking.plugin.toolkit.log.grpc.reporter.upstream_timeout=30"
fi
$_RUNJAVA ${PROJECTA_OPTS} -jar $DEMO_HOME/projectA/projectA.jar 2>/dev/null 1> /dev/null &
