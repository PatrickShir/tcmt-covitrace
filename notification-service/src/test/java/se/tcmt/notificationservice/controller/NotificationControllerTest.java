package se.tcmt.notificationservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.tcmt.notificationservice.AbstractIntegrationTest;
import se.tcmt.notificationservice.domain.Notification;
import se.tcmt.notificationservice.domain.NotificationStatus;
import se.tcmt.notificationservice.repository.NotificationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NotificationRepository notificationRepository;

    private Notification mockNotification;

    @BeforeEach
    void setUp() {
        String deviceId = "android-df5sd4fjt6e57x8gh9a2a3c";
        this.mockNotification = Notification.builder()
                .deviceId(deviceId)
                .status(NotificationStatus.RECEIVED)
                .build();
    }

    @Test
    void getNotEmptyNotificationHistoryByDeviceId() throws Exception {
        int page = 0;

        Notification notification = notificationRepository.save(mockNotification);

        mockMvc.perform(get("/api/notifications/history")
                .param("device-id", notification.getDeviceId())
                .param("page", String.valueOf(page))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        List<Notification> result = notificationRepository.findAllByDeviceId(notification.getDeviceId(), PageRequest.of(page,10, Sort.by("created")));

        assertThat(result).isNotEmpty();
    }

    @Test
    void getEmptyNotificationHistoryByDeviceId() throws Exception {
        int page = 0;
        String deviceId = "android-bf5sd4fjt6e57x8gh9a2a3c";

        mockMvc.perform(get("/api/notifications/history")
                .param("device-id", deviceId)
                .param("page", String.valueOf(page))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(notificationRepository.findAllByDeviceId(deviceId, PageRequest.of(page,10, Sort.by("created")))).isEmpty();
    }

    @Test
    @Disabled
    void blankDeviceIdShouldGetBAD_REQUEST() throws Exception {
        final int page = 0;

        mockMvc.perform(get("/api/notifications/history")
                .param("device-id", "")
                .param("page", String.valueOf(page))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateExistingReceivedNotification () throws Exception {

        Notification notification = notificationRepository.save(mockNotification);

        mockMvc.perform(patch("/api/notifications/view")
                .param("notification-id", notification.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();

        assertThat(updatedNotification.getStatus()).isEqualByComparingTo(NotificationStatus.VIEWED);
    }
}