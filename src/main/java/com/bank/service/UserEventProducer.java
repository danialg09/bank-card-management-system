package com.bank.service;

import com.bank.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.user-sync-topic}")
    private String topic;

    public void send(UserRegisteredEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.userId()), event);
    }
}
