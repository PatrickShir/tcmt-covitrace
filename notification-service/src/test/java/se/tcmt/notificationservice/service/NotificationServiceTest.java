package se.tcmt.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import se.tcmt.notificationservice.domain.Notification;
import se.tcmt.notificationservice.domain.NotificationStatus;
import se.tcmt.notificationservice.dto.NotificationDto;
import se.tcmt.notificationservice.exception.InvalidIdException;
import se.tcmt.notificationservice.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void getNotificationHistoryOfBlankOrEmptyDeviceIdShouldThrowInvalidDeviceIdException() {
        final String blankDeviceId = " ";
        final String emptyDeviceId = "";
        final int page = 1;
        assertThatThrownBy(() -> notificationService.getNotificationHistoryById(blankDeviceId, page)).isInstanceOf(InvalidIdException.class);
        assertThatThrownBy(() -> notificationService.getNotificationHistoryById(emptyDeviceId, page)).isInstanceOf(InvalidIdException.class);
    }

    @Test
    void getEmptyNotificationHistoryByValidDeviceId() {
        final String deviceId = "android-df5sd4fjt6e57x8gh9a2a3c";
        final int page = 1;

        List<NotificationDto> result = notificationService.getNotificationHistoryById(deviceId, page);
        assertThat(result).isEmpty();
    }

    @Test
    void getNotificationHistoryOfValidDeviceId() {
        String deviceId = "android-df5sd4fjt6e57x8gh9a2a3c";
        int page = 0;

        Notification mockNotification = Notification.builder()
                .deviceId(deviceId)
                .build();

        given(notificationRepository.findAllByDeviceId(anyString(), eq(PageRequest.of(page,10, Sort.by("created"))))).willReturn(List.of(mockNotification));

        List<NotificationDto> result = notificationService.getNotificationHistoryById(deviceId, page);

        assertThat(result).isNotEmpty();
    }

    @Test
    void updateNotificationWithBlankOrEmptyIdShouldThrowInvalidDeviceIdException() {
        final String blankDeviceId = " ";
        final String emptyDeviceId = "";
        assertThatThrownBy(() -> notificationService.updateReceivedNotification(blankDeviceId)).isInstanceOf(InvalidIdException.class);
        assertThatThrownBy(() -> notificationService.updateReceivedNotification(emptyDeviceId)).isInstanceOf(InvalidIdException.class);
    }

    @Test
    void updateReceivedNotificationTest() {
        UUID notificationId = UUID.randomUUID();
        String notificationUUID = notificationId.toString();

        Notification mockNotification = Notification.builder()
                .status(NotificationStatus.RECEIVED)
                .id(UUID.fromString(notificationUUID))
                .build();

        given(notificationRepository.save(any())).willReturn(mockNotification);
        given(notificationRepository.findById(any())).willReturn(Optional.of(mockNotification));

        NotificationDto notificationDto = notificationService.updateReceivedNotification(notificationUUID);

        assertThat(notificationDto.getStatus()).isEqualByComparingTo(NotificationStatus.VIEWED);

        then(notificationRepository).should(times(1)).findById(UUID.fromString(notificationUUID));
    }
}