package com.sweelam.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

//@Service
public class KafkaConsumerDetector {
    Logger log = LoggerFactory.getLogger(getClass());

    static int count = 0;

    @KafkaListener(
            topics = "${app.kafka.hack.topic-name}",
            concurrency = "30",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String record) {
        count++;

        if (count % 10000 == 0) {
            CompletableFuture.runAsync(() -> log.info("Consumed {} messages", count))
                    .thenRunAsync(() -> log.info("Last message {} ", record));
        }


    }
}
