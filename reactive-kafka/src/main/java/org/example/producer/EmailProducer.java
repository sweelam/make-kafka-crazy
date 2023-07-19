package org.example.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.config.KafkaConfig;
import org.example.constant.KafkaConstantEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.io.IOException;

public class EmailProducer {
    private static final Logger log = LoggerFactory.getLogger(EmailProducer.class);
    private KafkaSender<String, String> kafkaSender;
    private KafkaConfig kafkaConfig;

    public EmailProducer() {
        kafkaConfig = new KafkaConfig(KafkaConstantEnum.BOOTSTRAP_SERVER.value());
        this.kafkaSender = kafkaConfig.kafkaSender();
    }

    public void sendDummyMessagesTo(String topicName) {
        kafkaSender.send(Flux.range(1,10)
                        .map(i -> SenderRecord.create(new ProducerRecord<>(topicName, String.valueOf(i), "Messages #" + i), null)))
                .doOnNext(res -> System.out.printf("Message meta data %s \n", res.recordMetadata()))
                .doOnError(err -> err.printStackTrace())
//                .onErrorResume(th -> {
//                    if (th instanceof IOException) {
//                        log.error("IOException occurred {}", th.getCause());
//                    }
//                    return null;
//                })
                .doOnComplete(() -> System.out.println("Done"))
                .subscribe();
    }
}
