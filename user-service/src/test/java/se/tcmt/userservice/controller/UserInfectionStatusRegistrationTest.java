package se.tcmt.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.tcmt.userservice.AbstractIntegrationTest;
import se.tcmt.userservice.domain.User;
import se.tcmt.userservice.kafka.KafkaSender;
import se.tcmt.userservice.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserInfectionStatusRegistrationTest extends AbstractIntegrationTest {

    @MockBean
    KafkaSender kafkaSender;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        Timestamp created = new Timestamp(new Date().getTime());
        this.mockUser = User.builder()
                .deviceId(UUID.randomUUID().toString())
                .infected(false)
                .created(created)
                .updated(created)
                .build();
    }

    @Test
    void newlyCreatedUserShouldBeAbleToChangeInfectionStatus() throws Exception {
        doNothing().when(kafkaSender).publish(anyString(),anyString());

        User user = userRepository.save(mockUser);

        mockMvc.perform(patch("/api/users/infected")
                .param("device-id", user.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        User updatedUser = userRepository.findById(user.getDeviceId()).get();

        assertThat(updatedUser.isInfected()).isTrue();
    }

    @Test
    void infectionStatusOverTwoWeeksCanChangeStatusAgain() throws Exception {
        doNothing().when(kafkaSender).publish(anyString(),anyString());

        LocalDateTime created = LocalDateTime.now().minusMonths(3);
        LocalDateTime updated = LocalDateTime.now().plusWeeks(2);

        Timestamp time = Timestamp.valueOf(created);
        Timestamp time2 = Timestamp.valueOf(updated);

        mockUser.setCreated(time);
        mockUser.setUpdated(time2);
        mockUser.setInfected(true);

        User user = userRepository.save(mockUser);

        mockMvc.perform(patch("/api/users/infected")
                .param("device-id", user.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        User updatedUser = userRepository.findById(user.getDeviceId()).get();

        assertThat(updatedUser.isInfected()).isFalse();
    }
}