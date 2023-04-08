package com.example.springreactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaConsumerDetector {
    Logger log = LoggerFactory.getLogger(getClass());

    static AtomicInteger count = new AtomicInteger();

    @KafkaListener(
            topics = "${app.kafka.hack.topic-name}",
            concurrency = "10",
            groupId = "detector"
    )
    public void consume (String record) {
        count.addAndGet(1);

        if (count.get() % 10000 == 0) {

            log.info("Consumed {} messages", count);
            log.info("Last message {} " , record);
        }
    }
}
