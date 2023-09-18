package com.amelinroman.webfluxsecurity.security;

import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.exception.AuthException;
import com.amelinroman.webfluxsecurity.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;


/**
 * @author Amelin Roman
 * Компонент SecurityService отвечает за аутентификацию пользователей
 * и генерацию JWT-токенов.
 */

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSecond;
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Генерирует JWT-токен для указанного пользователя.
     *
     * @param user пользователь, для которого генерируется токен.
     * @return объект TokenDetails, содержащий сгенерированный токен и его параметры.
     */
    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};
        return generateToken(claims, user.getId().toString());
    }

    /**
     * Генерирует JWT-токен с указанными Claims и Subject.
     *
     * @param claims объект Map, содержащий Claims для токена.
     * @param subject значение Subject для токена.
     * @return объект TokenDetails, содержащий сгенерированный токен и его параметры.
     */
    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillis = expirationInSecond * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    /**
     * Генерирует JWT-токен с указанной датой окончания действия, Claims и Subject.
     *
     * @param expirationDate дата окончания действия токена.
     * @param claims объект Map, содержащий Claims для токена.
     * @param subject значение Subject для токена.
     * @return объект TokenDetails, содержащий сгенерированный токен и его параметры.
     */
    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    /**
     * Аутентифицирует пользователя с указанным именем пользователя и паролем
     * и возвращает JWT-токен.
     *
     * @param username имя пользователя пользователя.
     * @param password пароль пользователя.
     * @return Mono<TokenDetails> объект, содержащий сгенерированный токен и его параметры.
     * @throws AuthException если аутентификация неуспешна.
     */
    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new AuthException("Account disabled", "PROSELYTE_USER_ACCOUNT_DISABLED"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "PROSELYTE_INVALID_PASSWORD"));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "PROSELYTE_INVALID_USERNAME")));
    }
}
