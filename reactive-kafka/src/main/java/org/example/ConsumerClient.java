package org.example;

import org.example.consumer.EmailConsumer;

public class ConsumerClient {
    public static void main(String[] args) {
        var consumer = new EmailConsumer();
        consumer.listen();
    }
}
