package se.tcmt.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.tcmt.notificationservice.domain.Notification;
import se.tcmt.notificationservice.domain.NotificationStatus;
import se.tcmt.notificationservice.dto.NotificationDto;
import se.tcmt.notificationservice.exception.InvalidIdException;
import se.tcmt.notificationservice.repository.NotificationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationDto updateReceivedNotification(String notificationUUID) {

        if(notificationUUID.isBlank() || notificationUUID.isEmpty()) {
            throw new InvalidIdException("Invalid Notification ID! cannot be empty");
        }

        Notification notification = notificationRepository.findById(UUID.fromString(notificationUUID)).orElseThrow(() -> new EntityNotFoundException("Could not find notification with ID " + notificationUUID));
        NotificationDto updatedNotification = updateReceivedNotificationStatus(notification).convertToDto();
        return updatedNotification;
    }

    private Notification updateReceivedNotificationStatus(Notification notification) {
        notification.setStatus(NotificationStatus.VIEWED);
        return notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationHistoryById(String deviceID, int page) {
        if(deviceID.isBlank() || deviceID.isEmpty()) {
            throw new InvalidIdException("Invalid Device ID! cannot be empty");
        }

        List<Notification> notifications = notificationRepository.findAllByDeviceId(deviceID, PageRequest.of(page, 10, Sort.by("created")));
        return convertNotificationHistoryToDto(notifications);
    }

    private List<NotificationDto> convertNotificationHistoryToDto(List<Notification> notificationsToConvert) {
        return notificationsToConvert.stream().map(Notification::convertToDto).collect(Collectors.toList());
    }
}