package org.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
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

        return SenderOptions.<String,String>create(props)
                .maxInFlight(1024);
    }

    public KafkaSender<String, String> kafkaSender() {
        return KafkaSender.create(getSenderOption());
    }


    public ReceiverOptions<String, String> getReceiverOption(String topicName) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.boostrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "email-consumer-group3");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "email-consumer");


        return ReceiverOptions.<String,String>create(props)
                .consumerProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                .subscription(Collections.singleton(topicName));
    }

    public KafkaReceiver<String, String> kafkaReceiver(String topicName) {
        return KafkaReceiver.create(getReceiverOption(topicName));
    }
}
