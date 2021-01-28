#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$DEMO_HOME" ] && DEMO_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

AGENT_DIR=$DEMO_HOME/skywalking-agent
COLLECTOR_SERVER_LIST=localhost:18080

_RUNJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _RUNJAVA=java

if [ ! -d "$DEMO_HOME/kafka_2.11-2.3.0" ]; then
    tar -xvf $DEMO_HOME/kafka_2.11-2.3.0.tgz -C $DEMO_HOME
fi

if [ ! -f "$DEMO_HOME/logs" ]; then
    mkdir $DEMO_HOME/logs
fi

echo "kill kafka service"
ps -ef | grep eureka-service | awk '{print $2}' | xargs kill -9
echo "kill kafka service"
ps -ef | grep kafka_2.11-2.3.0 | awk '{print $2}' | xargs kill -9
echo "kill projectA service"
ps -ef | grep projectA | awk '{print $2}' | xargs kill -9
echo "kill projectB service"
ps -ef | grep projectB | awk '{print $2}' | xargs kill -9
echo "kill projectC service"
ps -ef | grep projectC | awk '{print $2}' | xargs kill -9
echo "kill projectD service"
ps -ef | grep projectD | awk '{print $2}' | xargs kill -9

#echo "start kafka service"
sh $DEMO_HOME/kafka_2.11-2.3.0/bin/zookeeper-server-start.sh $DEMO_HOME/kafka_2.11-2.3.0/config/zookeeper.properties 2>/dev/null 1> /dev/null &
sleep 20
sh $DEMO_HOME/kafka_2.11-2.3.0/bin/kafka-server-start.sh $DEMO_HOME/kafka_2.11-2.3.0/config/server.properties 2>/dev/null 1> /dev/null &

echo "start eureka service"
$_RUNJAVA -jar $DEMO_HOME/eureka-service/eureka-service.jar 2>/dev/null 1> /dev/null &

if [ $? -eq 0 ]; then
  sleep 1
	echo "eureka service started successfully!"
else
	echo "eureka service started failure!"
	exit 1
fi

echo "start project B service"
if [ -f "${AGENT_DIR}/skywalking-agent.jar" ]; then
    PROJECTB_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=projectB.business-zone -Dskywalking.logging.level=warn -Deureka.client.serviceUrl.defaultZone=http://192.168.252.12:8761/eureka/"
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
    PROJECTC_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=projectC.business-zone -Dskywalking.logging.level=warn -Deureka.client.serviceUrl.defaultZone=http://192.168.252.12:8761/eureka/"
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
    PROJECTD_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=projectD.business-zone -Dskywalking.logging.level=warn -Dkafka.server=192.168.252.12:9092"
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
    PROJECTA_OPTS="-javaagent:${AGENT_DIR}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER_LIST} -Dskywalking.agent.service_name=projectA.business-zone -Dskywalking.logging.level=warn -Deureka.client.serviceUrl.defaultZone=http://192.168.252.12:8761/eureka/"
fi
$_RUNJAVA ${PROJECTA_OPTS} -jar $DEMO_HOME/projectA/projectA.jar 2>/dev/null 1> /dev/null &
