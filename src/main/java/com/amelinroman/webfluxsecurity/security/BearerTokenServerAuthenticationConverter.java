package com.amelinroman.webfluxsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author Amelin Roman
 * Класс BearerTokenServerAuthenticationConverter отвечает за конвертацию токена аутентификации (Bearer token).
 * Используется для извлечения аутентификационного токена из заголовка авторизации HTTP-запроса.
 */

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtHandler jwtHandler;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Function<String, Mono<String>> getBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));

    /**
     * Конвертирует аутентификацию с помощью предоставленного ServerWebExchange.
     * Извлекает токен из заголовка авторизации, проверяет его с помощью JwtHandler
     * и создает объект UserAuthenticationBearer на основе проверенного токена.
     *
     * @param exchange ServerWebExchange содержащий информацию о клиентском запросе.
     * @return Mono<Authentication> с аутентификационной информацией пользователя.
     */
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(getBearerValue)
                .flatMap(jwtHandler::check)
                .flatMap(UserAuthenticationBearer::create);
    }

    /**
     * Извлекает значение заголовка авторизации из HTTP-запроса.
     *
     * @param webExchange ServerWebExchange содержащий информацию о клиентском запросе.
     * @return Mono<String> с информацией из заголовка авторизации.
     */
    private Mono<String> extractHeader(ServerWebExchange webExchange) {
        return Mono.justOrEmpty(webExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
