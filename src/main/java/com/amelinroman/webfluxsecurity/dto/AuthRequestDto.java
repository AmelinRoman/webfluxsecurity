package com.amelinroman.webfluxsecurity.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author Amelin Roman
 * Класс AuthRequestDto представляет собой объект передачи данных (Data Transfer Object)
 * для запроса аутентификации. Используется при отправке данных о пользователях (имя и пароль)
 * для проверки идентификации и аутентификации в системе.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequestDto {
    private String username;
    private String password;
}
