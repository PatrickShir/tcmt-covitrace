package se.tcmt.userservice.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.tcmt.userservice.dto.UserDto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @NotBlank(message = "Please provide a device id.")
    private String deviceId;
    private boolean infected;
    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;

    public UserDto convertToDto() {
        return UserDto.builder()
                .deviceId(this.deviceId)
                .infected(this.infected)
                .build();
    }
}