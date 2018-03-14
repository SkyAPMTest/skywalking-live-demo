package org.skywalking.springcloud.test.projecta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonCallService {

    @Autowired
    RestTemplate restTemplate;

    public String call(String name) {
        return restTemplate.getForObject("http://PROJECTB/projectB/" + name, String.class);
    }

}
