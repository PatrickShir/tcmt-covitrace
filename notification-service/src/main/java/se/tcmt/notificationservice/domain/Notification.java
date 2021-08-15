package se.tcmt.notificationservice.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import se.tcmt.notificationservice.dto.NotificationDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank(message = "deviceId cannot be blank")
    private String deviceId;
    private String message;
    private NotificationStatus status;

    @CreationTimestamp
    private Timestamp created;

    public NotificationDto convertToDto() {
        return NotificationDto.builder()
                .id(this.id)
                .message(this.message)
                .status(this.status)
                .build();
    }

}