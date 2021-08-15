package se.tcmt.notificationservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.tcmt.notificationservice.dto.NotificationDto;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationTest {

    private Notification mockNotification;
    private NotificationDto mockNotificationDto;

    @BeforeEach
    void setUp(){
        mockNotification = Notification.builder()
                .deviceId("android-8cv29yf4aedf7x1cf2gw45era9w8")
                .message("StatusMessage")
                .status(NotificationStatus.SENT)
                .build();

        mockNotificationDto = NotificationDto.builder()
                .message("StatusMessage")
                .status(NotificationStatus.VIEWED)
                .build();
    }

    @Test
    void convertToDtoTest() {
        assertThat(mockNotification.convertToDto()).isInstanceOf(NotificationDto.class);
    }

    @Test
    void convertToEntityTest() {
        assertThat(mockNotificationDto.convertToEntity()).isInstanceOf(Notification.class);
    }

}