package se.tcmt.hrcservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.tcmt.hrcservice.domain.User;

@Service
@Slf4j
public class TestService {

    public void logIncomingMessage(User user) {
        String jsonUser = null;
        try {
            jsonUser = new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
        }
        log.info("JAVA USER OBECT FROM USER-SERVICE " + jsonUser);
    }
}
