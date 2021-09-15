package test.skywalking.springcloud.test.projecta.service;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import test.skywalking.springcloud.test.projecta.vo.Result;

import static test.skywalking.springcloud.test.projecta.config.Constant.SAMPLE_RATE;

@RestController
public class ServiceController {
    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    private final AtomicLong counter = new AtomicLong(0);

    @Autowired
    private RibbonCallService projectBServiceCall;

    @Autowired
    private FeignCallService projectCServiceCall;

    @RequestMapping(value = "/projectA/{name}")
    @ResponseBody
    public Result hi(@PathVariable(required = false) String name) {
        if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
            logger.debug("calling /projectA/{name} service");
        }
        projectBServiceCall.call(name);
        projectCServiceCall.call(name);
        return new Result(TraceContext.traceId());
    }
}
