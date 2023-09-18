package com.amelinroman.webfluxsecurity.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Amelin Roman
 * Класс TokenDetails содержит информацию о JWT-токене и его сроке действия.
 * Этот класс используется для передачи информации о токене между методами и слоями приложения.
 */

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
