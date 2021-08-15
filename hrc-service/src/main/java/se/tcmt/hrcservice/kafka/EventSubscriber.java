package se.tcmt.hrcservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.tcmt.hrcservice.domain.User;
import se.tcmt.hrcservice.service.TestService;

@Component
@RequiredArgsConstructor
public class EventSubscriber {

    private final TestService testService;

    @KafkaListener(topics = "tester-1", containerFactory = "userKafkaListenerContainerFactory", groupId = "covitrace")
    public void consumeMessage(User user) {
        testService.logIncomingMessage(user);
    }
}
