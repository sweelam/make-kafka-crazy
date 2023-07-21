package com.sweelam.streams.config;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

public class KafkaConfig {
    private static KafkaStreams kafkaStreams;

    /**
     * Connect and close stream on program exit only
     * 
     * @param topology
     */
    public static void connect(final Topology topology) {
        if (null != kafkaStreams && kafkaStreams.state().isRunningOrRebalancing()) {
            return;
        }
        Properties props = readProps();
        kafkaStreams = new KafkaStreams(topology, props);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        kafkaStreams.start();
    }

    private static Properties readProps() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

}