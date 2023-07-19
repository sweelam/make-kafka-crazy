package org.example;

import org.example.producer.EmailProducer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var emailProducer = new EmailProducer();
        emailProducer.sendDummyMessagesTo("hack-topic");
        Thread.currentThread().join();
    }
}