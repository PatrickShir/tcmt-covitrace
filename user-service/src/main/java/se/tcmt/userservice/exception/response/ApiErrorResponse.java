package se.tcmt.userservice.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ApiErrorResponse {

    private final String exceptionType;
    private final String message;
    private final HttpStatus status;
    private final String timestamp;
}
