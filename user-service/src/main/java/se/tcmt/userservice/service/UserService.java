package se.tcmt.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.tcmt.userservice.domain.User;
import se.tcmt.userservice.dto.UserDto;
import se.tcmt.userservice.exception.InfectionStatusException;
import se.tcmt.userservice.kafka.KafkaSender;
import se.tcmt.userservice.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaSender kafkaSender;

    public UserDto handleUserRegistration(String deviceId) {

        Optional<User> userOptional = this.userRepository.findById(deviceId);

        return createOrReturnExistingUser(userOptional, deviceId);
    }

    private UserDto createOrReturnExistingUser(Optional<User> userOptional, String deviceId) {
        if (userOptional.isEmpty()) {
            User user = User.builder()
                    .deviceId(deviceId)
                    .build();

            return userRepository.save(user).convertToDto();
        }

        return userOptional.get().convertToDto();
    }

    public UserDto changeInfectionStatus(String deviceId) {
        User user = this.userRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find user with id " + deviceId));

        if (!hasPassedTwoWeeksMinimumRecoveryTime(user))
            throw new InfectionStatusException("Unable to change infection status since it has not been at least" +
                    " two weeks since the last change.");

        UserDto updatedUser = updateStatus(user).convertToDto();
        notifyHrcOfInfectedUser(updatedUser.isInfected(), deviceId);
        return updatedUser;
    }

    private void notifyHrcOfInfectedUser(boolean isInfected, String deviceId) {
        if (isInfected)
            kafkaSender.publish("infection-contact", deviceId);
    }

    private User updateStatus(User user) {
        user.setInfected(!user.isInfected());
        return userRepository.save(user);
    }

    private boolean hasPassedTwoWeeksMinimumRecoveryTime(User user) {
        if (user.getCreated().equals(user.getUpdated()))
            return true;

        LocalDateTime twoWeeksLater = user.getUpdated()
                    .toLocalDateTime()
                    .plusWeeks(2);
        Timestamp minimumRecoveryTime = Timestamp.valueOf(twoWeeksLater);

        Timestamp presentTime = new Timestamp(new Date().getTime());
        return presentTime.after(minimumRecoveryTime);
    }
}
