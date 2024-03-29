package test.skywalking.springcloud.test.projectc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import static test.skywalking.springcloud.test.projectc.config.Constant.SAMPLE_RATE;

@Component
public class HttpClientCaller {
    private static final Logger logger = LogManager.getLogger(HttpClientCaller.class);

    private final AtomicLong counter = new AtomicLong(0);

    public void call(String url) throws IOException {
        if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
            logger.debug("calling {}", url);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuffer response = new StringBuffer();

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }

        reader.close();
        httpClient.close();
    }
}
