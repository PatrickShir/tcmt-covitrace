package se.tcmt.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.tcmt.userservice.domain.User;
import se.tcmt.userservice.dto.UserDto;
import se.tcmt.userservice.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    //TODO: create unit tests for service class

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void validIdShouldReturnExistingUser() {
        String deviceId = "android-df5sd4fjt6e57x8gh9a2a3c";
        User mockUser = User.builder()
                .deviceId(deviceId)
                .build();

        given(userRepository.findById(anyString())).willReturn(Optional.of(mockUser));

        UserDto userDto = userService.handleUserRegistration(deviceId);

        assertThat(userDto.getDeviceId().length()).isEqualTo(deviceId.length());

        then(userRepository).should(times(1)).findById(deviceId);

    }

    @Test
    void nonExistingUserShouldGetStored() {
        //given
        String deviceId = "android-df5sd4fjt6e57x8gh9a2a3c";
        User mockUser = User.builder()
                .deviceId(deviceId)
                .build();
        given(userRepository.findById(anyString())).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(mockUser);

        //when
        userService.handleUserRegistration(deviceId);


        //then
        then(userRepository).should(times(1)).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getDeviceId()).isEqualTo(deviceId);
    }

}
