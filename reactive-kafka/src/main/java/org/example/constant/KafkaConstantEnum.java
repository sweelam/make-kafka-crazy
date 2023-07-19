package org.example.constant;

public enum KafkaConstantEnum {
    BOOTSTRAP_SERVER("localhost:9092");

    private String value;

    public String value() {
        return value;
    }

    KafkaConstantEnum(String value) {
        this.value = value;
    }
}
