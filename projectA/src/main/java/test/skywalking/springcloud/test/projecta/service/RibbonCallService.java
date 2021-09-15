package test.skywalking.springcloud.test.projecta.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonCallService {
    final RestTemplate restTemplate;

    public RibbonCallService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String call(String name) {
        return restTemplate.getForObject("http://projectb/projectB/{name}", String.class, name);
    }
}
