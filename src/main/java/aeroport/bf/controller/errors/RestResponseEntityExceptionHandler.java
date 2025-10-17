package aeroport.bf.controller.errors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {

        return handleExceptionInternal(ex,
                ErrorResponse.builder()
                        .status(ex.getStatusCode().value())
                        .message(ex.getReason())
                        .debugMessage(ex.getLocalizedMessage())
                        .errorDate(LocalDateTime.now())
                        .build(),
                new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request) {

        return handleExceptionInternal(ex,
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Unexpected internal error.")
                        .debugMessage(ex.getLocalizedMessage())
                        .errorDate(LocalDateTime.now())
                        .build(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
