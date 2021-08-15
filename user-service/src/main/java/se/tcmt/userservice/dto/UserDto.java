package se.tcmt.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import se.tcmt.userservice.domain.User;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    @Id
    @NotBlank(message = "Please provide a [deviceId].")
    private String deviceId;
    private boolean infected;

    public User convertToEntity() {
        return User.builder()
                .deviceId(this.deviceId)
                .infected(this.infected)
                .build();
    }
}
