package mate.academy.bookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalException extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String STATUS_FIELD = "status";
    private static final String ERRORS_FIELD = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_FIELD, LocalDateTime.now());
        body.put(STATUS_FIELD, HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put(ERRORS_FIELD, errors);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError fieldError) {
            String field = fieldError.getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler({RegistrationException.class})
    protected ResponseEntity<Object> handleRegistrationException(
            RegistrationException ex,
            HttpHeaders headers,
            HttpStatusCode status
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_FIELD, LocalDateTime.now());
        body.put(STATUS_FIELD, HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getMessage().lines().toList();
        body.put(ERRORS_FIELD, errors);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }
}
