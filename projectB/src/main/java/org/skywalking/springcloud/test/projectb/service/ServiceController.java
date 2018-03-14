package org.skywalking.springcloud.test.projectb.service;

import java.util.Random;
import java.util.UUID;
import org.skywalking.springcloud.test.projectb.dao.DatabaseOperateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @Autowired
    private DatabaseOperateDao operateDao;

    @RequestMapping("/projectB/{value}")
    public String home(@PathVariable("value") String value) throws InterruptedException {
        Thread.sleep(new Random().nextInt(2) * 1000);
        operateDao.saveUser(value);
        operateDao.selectUser(value);
        return value + "-" + UUID.randomUUID().toString();
    }
}
