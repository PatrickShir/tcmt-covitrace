package se.tcmt.userservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String topicName, String deviceId) {
        kafkaTemplate.send(topicName, deviceId);
    }
}
