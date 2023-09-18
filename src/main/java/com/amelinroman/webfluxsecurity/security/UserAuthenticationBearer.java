package com.amelinroman.webfluxsecurity.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Amelin Roman
 * Класс UserAuthenticationBearer предоставляет метод для создания объекта аутентификации
 * на основе результатов верификации JWT-токена.
 */
public class UserAuthenticationBearer {

    /**
     * Создает объект аутентификации с основной информацией о пользователе, извлеченной из верифицированных Claims JWT-токена.
     *
     * @param verificationResult Результат верификации JWT-токена, содержащий Claims токена.
     * @return Mono<Authentication> объект аутентификации с информацией о пользователе из JWT-токена.
     */
    public static Mono<Authentication> create(JwtHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();
        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        Long principalId = Long.parseLong(subject);
        CustomPrincipal principal = new CustomPrincipal(principalId, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}
