package ar.vicria.subte.microservice.controllers;

import ar.vicria.subte.microservice.configuration.MessageSource;
import ar.vicria.subte.microservice.dtos.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
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
public class GlobalControllerExceptionHandler {

    private final MessageSource messages;

    private final static List<String> BAD_REQUEST_EXCEPTIONS = Arrays.asList(
            "duplicate key value violates unique constrain",
            "violates not-null constraint"
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorDto dto = convertValidationErrors(ex.getBindingResult().getAllErrors());
        return Mono.just(dto);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<ErrorDto> handleBindException(BindException ex) {
        ErrorDto dto = convertValidationErrors(ex.getAllErrors());
        return Mono.just(dto);
    }

    /**
     * EntityNotFoundException.
     * @param ex EntityNotFoundException
     * @return HttpStatus.NOT_FOUND
     */
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Mono<ErrorDto> handleEntityNotFoundExceptions(RuntimeException ex) {
        return Mono.just(new ErrorDto(ex.getLocalizedMessage()));
    }

    /**
     * Exception.
     * @param ex Exception
     * @return INTERNAL_SERVER_ERROR or BAD_REQUEST for posgresqlEx
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mono<ResponseEntity<ErrorDto>> handleAllExceptions(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String stackTraceAsString = getStackTraceAsString(ex);
        for (String badRequestException : BAD_REQUEST_EXCEPTIONS) {
            if (stackTraceAsString.contains(badRequestException)) {
                status = HttpStatus.BAD_REQUEST;
                break;
            }
        }

        ErrorDto dto = new ErrorDto(ex.getLocalizedMessage(), getStackTraceAsString(ex));
        return Mono.just(ResponseEntity.status(status).body(dto));
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<ErrorDto> handleHttpMessageNotReadable(ServerWebInputException ex) {
        return Mono.just(new ErrorDto(ex.getLocalizedMessage()));
    }

    private ErrorDto convertValidationErrors(List<ObjectError> objectErrors) {
        ErrorDto dto = new ErrorDto();
        List<String> errors = new ArrayList<>();
        objectErrors.forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                if (errorMessage == null) {
                    errorMessage = messages.getMessage(error.getCode(), error.getArguments());
                }
                errors.add(String.join(":", fieldName, errorMessage));
            }
        });

        dto.setMessage(String.join("/n", errors));

        return dto;
    }

}
