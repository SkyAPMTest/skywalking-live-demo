/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package test.skywalking.springcloud.test.projectd;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.toolkit.trace.Trace;

import static test.skywalking.springcloud.test.projectd.config.Constant.SAMPLE_RATE;

public class MessageConsumer extends Thread {

    private Logger logger = LogManager.getLogger(MessageConsumer.class);
    private AtomicLong counter = new AtomicLong(0);
    private final KafkaConsumer<String, String> consumer;

    public MessageConsumer(String bootstrapServers, String topicName) {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", bootstrapServers);
        consumerProperties.put("group.id", "test");
        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("auto.offset.reset", "latest");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Arrays.asList(topicName));
    }

    @Override
    public void run() {
        while (true) {
            consumer();
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Trace
    public void consumer() {
        ConsumerRecords<String, String> records = consumer.poll(100);
        if (!records.isEmpty()) {
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(new String(record.headers().headers("TEST").iterator().next().value()));
            }
            if (counter.getAndIncrement() % SAMPLE_RATE == 0) {
                logger.info("consumer {}  message success.", records.count());
            }
        }

    }

}
