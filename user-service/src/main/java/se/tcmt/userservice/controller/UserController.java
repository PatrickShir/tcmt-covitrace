package se.tcmt.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.tcmt.userservice.dto.UserDto;
import se.tcmt.userservice.service.UserService;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserDto> registerDevice(@RequestParam(name = "device-id")
                                                  @NotBlank(message = "Please provide a [device-id].")
                                                          String deviceId) {
        UserDto userDto = userService.handleUserRegistration(deviceId);
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("infected")
    public ResponseEntity<UserDto> updateInfected(@RequestParam(name = "device-id")
                                                  @NotBlank(message = "Please provide a [device-id].")
                                                          String deviceId) {
        UserDto updatedUserDto = userService.changeInfectionStatus(deviceId);
        return ResponseEntity.ok(updatedUserDto);
    }
}
