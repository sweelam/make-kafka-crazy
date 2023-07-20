package org.example;

import org.example.producer.EmailProducer;

public class ProducerClient {
    public static void main(String[] args) throws InterruptedException {
        var emailProducer = new EmailProducer();
        emailProducer.sendDummyMessagesTo("hack-topic");

        // block system termination
        Thread.currentThread().join();
    }
}