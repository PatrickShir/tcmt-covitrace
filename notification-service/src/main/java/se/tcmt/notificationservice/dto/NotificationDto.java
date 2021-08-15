package se.tcmt.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import se.tcmt.notificationservice.domain.Notification;
import se.tcmt.notificationservice.domain.NotificationStatus;

import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NotificationDto {

    @Id
    private UUID id;
    private String message;
    private NotificationStatus status;

    public Notification convertToEntity() {
        return Notification.builder()
                .id(this.id)
                .message(this.message)
                .status(this.status)
                .build();
    }
}