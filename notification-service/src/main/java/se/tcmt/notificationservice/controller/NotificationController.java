package se.tcmt.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.tcmt.notificationservice.dto.NotificationDto;
import se.tcmt.notificationservice.service.NotificationService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/history")
    public ResponseEntity<?> getNotificationHistory(@RequestParam(name = "device-id") @NotBlank(message = "Please provide a [device-id].") String deviceId, @RequestParam(name = "page") int page) {
        List<NotificationDto> notificationDTOs = notificationService.getNotificationHistoryById(deviceId, page);
        return ResponseEntity.ok(notificationDTOs);
    }

    @PatchMapping("/view")
    public ResponseEntity<?> updateReceivedNotification(@RequestParam(name = "notification-id")@NotBlank(message = "Please provide a [notificationI-id].") String notificationId) {
        NotificationDto updatedNotification = notificationService.updateReceivedNotification(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }
}