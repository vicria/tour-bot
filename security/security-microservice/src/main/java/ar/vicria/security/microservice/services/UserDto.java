package ar.vicria.security.microservice.services;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserDto {
    String login;
    String name;
    Boolean active;
    LocalDateTime creationTime;
    String employeeId;
    String id;
    LocalDateTime lastLogin;
    LocalDateTime lastModifiedTime;
    String password = "[PROTECTED]";
}
