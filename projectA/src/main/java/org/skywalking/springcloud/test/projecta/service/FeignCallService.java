package org.skywalking.springcloud.test.projecta.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "projectC")
public interface FeignCallService {

    @RequestMapping(value = "/projectC/{name}", method = RequestMethod.GET)
    String call(@PathVariable(value = "name") String name);
}
