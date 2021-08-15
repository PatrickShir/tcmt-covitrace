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
import se.tcmt.userservice.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class InfectionStatusRegistrationMockTest extends AbstractIntegrationTest {

    @MockBean
    UserRepository mockUserRepository;

    @Autowired
    MockMvc mockMvc;

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
    void infectionStatusUnderTwoWeeksCannotChangeStatus() throws Exception {
        LocalDateTime created = LocalDateTime.now().minusMonths(1);
        LocalDateTime updated = LocalDateTime.now().minusWeeks(1);
        Timestamp time = Timestamp.valueOf(created);
        Timestamp time2 = Timestamp.valueOf(updated);

        mockUser.setCreated(time);
        mockUser.setUpdated(time2);

        when(mockUserRepository.findById(anyString())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(patch("/api/users/infected")
                .param("device-id", mockUser.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }
}