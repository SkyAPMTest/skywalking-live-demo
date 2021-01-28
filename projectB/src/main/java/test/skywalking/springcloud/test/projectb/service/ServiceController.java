package test.skywalking.springcloud.test.projectb.service;

import java.util.Random;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.skywalking.springcloud.test.projectb.dao.DatabaseOperateDao;

@RestController
public class ServiceController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    @Autowired
    private DatabaseOperateDao operateDao;

    @RequestMapping("/projectB/{value}")
    public String home(@PathVariable("value") String value) throws InterruptedException {
        logger.debug("calling /projectB/{value}");
        Thread.sleep(new Random().nextInt(2) * 1000);
        operateDao.saveUser(value);
        operateDao.selectUser(value);
        return value + "-" + UUID.randomUUID().toString();
    }
}
