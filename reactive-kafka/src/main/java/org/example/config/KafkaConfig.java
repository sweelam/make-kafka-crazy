package org.example.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {
    private String boostrapServers;
    public KafkaConfig(String bootstrapServer) {
        this.boostrapServers = bootstrapServer;
    }

    public SenderOptions<String, String> getSenderOption() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.boostrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000);

        return SenderOptions.<String,String>create(props)
                .maxInFlight(1024);
//                .closeTimeout(Duration.ofSeconds(2));
    }

    public KafkaSender<String, String> kafkaSender() {
        return KafkaSender.create(getSenderOption());
    }
}
