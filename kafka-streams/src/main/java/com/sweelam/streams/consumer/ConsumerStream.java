package com.sweelam.streams.consumer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class ConsumerStream {
    private static final Logger logger = Logger.getLogger(ConsumerStream.class.getName());

    /**
     * Build the topology
     * 
     * @param inputTopic
     * @return
     */
    public static Topology buildTopology(final String inputTopic, final String outputTopic) {
        var builder = new StreamsBuilder();

        final Consumed<String, String> consumerConfig = 
            Consumed.with(Serdes.String(), Serdes.String(), null, AutoOffsetReset.EARLIEST);
            

        final KStream<String, String> hackStream = builder.stream(inputTopic, consumerConfig);

        hackStream
                .mapValues(value -> (Integer.valueOf(value.replaceAll("[^0-9]", ""))))
                .filter((key, value) -> value % 2 == 0)
                .peek((key, value) -> logger.log(Level.INFO, " Value received {0}", value))
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.Integer()));

        
        var topology =  builder.build();      
        System.out.println(topology.describe());

        return topology;
    }
}
