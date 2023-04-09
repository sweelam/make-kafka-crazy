package com.example.springreactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("kafka")
public class KafkaController {
    private final KafkaTemplate<String, String> kt;
    private final String topicName;
    private final ExecutorService ex = Executors.newCachedThreadPool();

    public KafkaController(KafkaTemplate<String, String> kt,
                           @Value("${app.kafka.hack.topic-name}") final String topicName) {
        this.kt = kt;
        this.topicName = topicName;
    }

    /**
     * Message produced evenly across all partitions , and consume evenly across all consumers
     *
     * @param limit
     * @return
     */
    @PostMapping("/push/{limit}")
    public ResponseEntity<String> tryMeHeavily(@PathVariable int limit) {
        var startTime = System.nanoTime();

        push(limit);

        return ResponseEntity.ok("tryMeHeavily took " + ((System.nanoTime() - startTime) / 1000_000_000) + "seconds");
    }

    /**
     * Message produced asynchronously evenly across all partitions , and consume evenly across all consumers
     *
     * @param limit
     * @return
     */
    @PostMapping("/push/async/{limit}")
    public Mono<ResponseEntity<String>> tryMeHeavilyAsync(@PathVariable int limit) {
        var startTime = System.nanoTime();

        CompletableFuture.runAsync(() -> push(limit), ex);

        return Mono.just(
                ResponseEntity.ok("tryMeHeavily took " + ((System.nanoTime() - startTime) / 1000_000_000) + "seconds")
        );
    }

    private void push(int limit) {
        IntStream.range(0, limit)
                .parallel()
                .forEach(val -> {
                    String msg = "Message-" + val + " created!";
                    kt.send(topicName, msg);
                });
    }

    /**
     * As message is published with a key "order-Q", it always goes to the same partition
     * In such case single instance/consumer will consume
     *
     * @param limit
     * @return
     */
    @PostMapping("/push/ordered/{limit}")
    public ResponseEntity<String> letMeBeAQueue(@PathVariable int limit) {
        var startTime = System.nanoTime();

        IntStream.range(0, limit)
                .forEach(val -> {
                    String msg = "Order-" + val + " created!";
                    kt.send(topicName, "order-Q", msg);
                });

        return ResponseEntity.ok("tryMeHeavily took " + ((System.nanoTime() - startTime) / 1000_000_000) + "seconds");
    }


}
