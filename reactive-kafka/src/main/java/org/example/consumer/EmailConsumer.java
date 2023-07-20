package org.example.consumer;

import org.example.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

import static org.example.constant.KafkaConstantEnum.BOOTSTRAP_SERVER;
import static org.example.constant.KafkaConstantEnum.HACK_TOPIC_NAME;

public class EmailConsumer {
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);
    private Flux<ReceiverRecord<String, String>> inboundData;
    private KafkaConfig kafkaConfig;

    public EmailConsumer() {
        kafkaConfig = new KafkaConfig(BOOTSTRAP_SERVER.value());
        this.inboundData =  kafkaConfig.kafkaReceiver(HACK_TOPIC_NAME.value()).receive();
    }

    public void listen() {
        Disposable disposable = inboundData.doOnNext(record -> {
            log.info("Received {}", record.value());
            record.receiverOffset().acknowledge();
        }).subscribe();

        if (disposable.isDisposed()) {
            log.info("Subscription stopped!!!");
        }
    }

}
