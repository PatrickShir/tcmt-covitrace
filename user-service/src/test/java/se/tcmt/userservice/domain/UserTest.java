package se.tcmt.userservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.tcmt.userservice.dto.UserDto;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User mockUser;
    private UserDto mockUserDto;

    @BeforeEach
    void setUp() {
        String deviceId = "android-8cv29yf4aedf7x1cf2gw45era9w8";
        mockUser = User.builder()
                .deviceId(deviceId)
                .infected(true)
                .created(new Timestamp(new Date().getTime()))
                .updated(null)
                .build();

        mockUserDto = UserDto.builder()
                .deviceId(deviceId)
                .infected(false)
                .build();
    }

    @Test
    void convertToDtoTest() {
        assertThat(mockUser.convertToDto()).isInstanceOf(UserDto.class);
    }

    @Test
    void convertToEntityTest() {
        assertThat(mockUserDto.convertToEntity()).isInstanceOf(User.class);
    }
}
