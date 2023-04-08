package com.example.springreactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("kafka")
public class KafkaController {
    Logger log = LoggerFactory.getLogger(getClass());
    private final KafkaTemplate<String, String> kt;
    private final String topicName;
    private final ExecutorService ex = Executors.newCachedThreadPool();;

    public KafkaController(KafkaTemplate<String, String> kt,
                           @Value("${app.kafka.hack.topic-name}") final String topicName) {
        this.kt = kt;
        this.topicName = topicName;
    }

    @GetMapping("/push/{limit}")
    public ResponseEntity<String> tryMeHeavily(@PathVariable int limit) {

        IntStream.range(1, limit)
                .parallel()
                .forEach(val -> {
                    String msg = "Message-" + val + " created!";
                    kt.send(topicName, msg);
                });

        return ResponseEntity.ok("Done");
    }

}
