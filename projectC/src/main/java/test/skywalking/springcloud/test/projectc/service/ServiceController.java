package test.skywalking.springcloud.test.projectc.service;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static test.skywalking.springcloud.test.projectc.config.Constant.SAMPLE_RATE;

@RestController
public class ServiceController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    @Value("${bootstrap.servers:127.0.0.1:9092}")
    private String bootstrapServers;

    private final String topicName = "test-trace-topic";

    private final AtomicLong counter = new AtomicLong(0);

    private final HttpClientCaller httpClientCaller;

    private Properties producerProperties;

    public ServiceController(
        final HttpClientCaller httpClientCaller) {
        this.httpClientCaller = httpClientCaller;
    }

    @PostConstruct
    public void setUp() {
        producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", bootstrapServers);
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("auto.create.topics.enable", "true");
        producerProperties.put(
            "key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put(
            "value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    }

    @RequestMapping("/projectC/{value}")
    public String home(@PathVariable("value") String value)
        throws InterruptedException, IOException {
        if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
            logger.debug("calling /projectC/{value}");
        }
        httpClientCaller.call("http://www.baidu.com");

        Producer<String, String> producer = new KafkaProducer<>(producerProperties);
        ProducerRecord<String, String> record =
            new ProducerRecord<String, String>(topicName, Integer.toString(1), Integer.toString(1));
        record.headers().add("TEST", "TEST".getBytes());
        producer.send(record);
        producer.close();

        return value + "-" + UUID.randomUUID().toString();
    }
}
