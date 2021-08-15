package se.tcmt.userservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import se.tcmt.userservice.exception.InfectionStatusException;
import se.tcmt.userservice.exception.response.ApiErrorResponse;

import javax.persistence.EntityNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(Exception e) {
        return getAndLogApiException(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(InfectionStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDeviceIdException(Exception e) {
        return getAndLogApiException(HttpStatus.FORBIDDEN, e);
    }

    private ResponseEntity<ApiErrorResponse> getAndLogApiException(HttpStatus status, Exception e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(ApiErrorResponse.builder()
                .exceptionType(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(getTimeOfError())
                .status(status)
                .build(), status);
    }

    private String getTimeOfError() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timeOfError = dateFormat.format(new Date());

        return timeOfError;
    }
}
