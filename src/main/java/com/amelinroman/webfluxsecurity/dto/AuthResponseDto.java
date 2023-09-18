package com.amelinroman.webfluxsecurity.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Amelin Roman
 * Класс AuthResponseDto представляет собой объект передачи данных (Data Transfer Object)
 * для ответа аутентификации. Используется при предоставлении информации о токене и сроках действия
 * после успешной аутентификации пользователя.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponseDto {

    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
