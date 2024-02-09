package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.microservice.configuration.MessageSource;
import ar.vicria.subte.microservice.dtos.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Throwables.getStackTraceAsString;

/**
 * General handler for all exceptions.
 */
@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messages;

    private final static List<String> BAD_REQUEST_EXCEPTIONS = Arrays.asList(
            "duplicate key value violates unique constrain",
            "violates not-null constraint"
    );

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ErrorDto dto = convertValidationErrors(ex.getBindingResult().getAllErrors());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    /**
     * EntityNotFoundException.
     * @param ex EntityNotFoundException
     * @return HttpStatus.NOT_FOUND
     */
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleEntityNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception.
     * @param ex Exception
     * @return INTERNAL_SERVER_ERROR or BAD_REQUEST for posgresqlEx
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String stackTraceAsString = getStackTraceAsString(ex);
        for (String badRequestException : BAD_REQUEST_EXCEPTIONS) {
            if (stackTraceAsString.contains(badRequestException)) {
                status = HttpStatus.BAD_REQUEST;
                break;
            }
        }

        ErrorDto dto = new ErrorDto(ex.getLocalizedMessage(), getStackTraceAsString(ex));
        logger.error(messages.getMessage("unexpected.error", dto.getUuid()), ex);
        return ResponseEntity.status(status).body(dto);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ErrorDto(ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    private ErrorDto convertValidationErrors(List<ObjectError> objectErrors) {
        ErrorDto dto = new ErrorDto();
        List<String> errors = new ArrayList<>();
        objectErrors.forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                if (errorMessage == null) {
                    errorMessage = messages.getMessage(fieldError.getCode(), fieldError.getArguments());
                }
                errors.add(String.join(":", fieldName, errorMessage));
            }
        });

        dto.setMessage(String.join("/n", errors));

        return dto;
    }

}
