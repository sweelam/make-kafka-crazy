package com.sweelam;

import com.sweelam.streams.Constants;
import com.sweelam.streams.config.KafkaConfig;

import static com.sweelam.streams.consumer.ConsumerStream.*;
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        var topology = buildTopology(Constants.INPUT_TOPIC, Constants.OUTPUT_TOPIC);
        KafkaConfig.connect(topology);
    }
}
