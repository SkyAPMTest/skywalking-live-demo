# SkyWalking Live Demo

## Quick Start
### Build live demo
The live demo package is in this project base directory after you execute the following commands.
```
> git clone https://github.com/SkywalkingTest/skywalking-live-demo.git
> cd skywalking-live-demo 
> mvn clean package # build the live demo archive
```

### Deploy and start live demo
Visit the `http://localhost:8764/projectA/test` in your browser after execute the following commands.
*Note:* 
1. Start collector service before you execute the following commands.
2. those variables needs to be set according to the actual situation.
   * `COLLECTOR_SERVER_LIST`: The collector server addresses 
   * `AGENT_DIR`: The SkyWalking agent home
```
> export AGENT_DIR=${AGENT_HOME}
> export COLLECTOR_SERVER_LIST=localhost:10800
> cd test-demo-assembly/bin
> ./startup.sh
```