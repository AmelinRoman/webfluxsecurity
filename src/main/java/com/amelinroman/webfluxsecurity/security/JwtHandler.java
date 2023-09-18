package com.amelinroman.webfluxsecurity.security;

import com.amelinroman.webfluxsecurity.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

/**
 * @author Amelin Roman
 * Класс JwtHandler отвечает за проверку JWT-токенов, используя секретный ключ.
 */
public class JwtHandler {

    private final String secret;

    /**
     * Конструктор класса JwtHandler.
     *
     * @param secret секретный ключ для верификации JWT-токенов.
     */
    public JwtHandler(String secret) {
        this.secret = secret;
    }

    /**
     * Проверяет JWT-токен и возвращает результат проверки.
     *
     * @param accessToken JWT-токен, который требуется проверить.
     * @return Mono<VerificationResult> содержащий результат проверки токена.
     */
    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    /**
     * Выполняет проверку JWT-токена и возвращает результат проверки.
     *
     * @param token JWT-токен, который требуется проверить.
     * @return объект VerificationResult с результатами проверки токена.
     * @throws RuntimeException если токен истек.
     */
    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    /**
     * Извлекает Claims из JWT-токена.
     *
     * @param token JWT-токен, из которого требуется извлечь Claims.
     * @return объект Claims, содержащий информацию о привилегиях и атрибутах токена.
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Класс VerificationResult содержит результаты проверки JWT-токена,
     * включая Claims и сам токен.
     */
    public static class VerificationResult {
        public Claims claims;
        public String token;

        /**
         * Конструктор класса VerificationResult.
         *
         * @param claims объект Claims, содержащий информацию о привилегиях и атрибутах токена.
         * @param token JWT-токен, который был проверен.
         */
        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
