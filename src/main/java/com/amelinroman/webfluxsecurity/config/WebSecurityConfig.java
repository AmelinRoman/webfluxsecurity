package com.amelinroman.webfluxsecurity.config;

import com.amelinroman.webfluxsecurity.security.AuthenticationManager;
import com.amelinroman.webfluxsecurity.security.BearerTokenServerAuthenticationConverter;
import com.amelinroman.webfluxsecurity.security.JwtHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

/**
 * Конфигурация безопасности на основе {@link WebSecurityConfig} для веб-приложений,
 * использующих реактивную архитектуру. Класс использует аннотацию \@Slf4j для
 * логирования.
 * Этот класс настраивает правила доступа к различным маршрутам и обработку ошибок
 * аутентификации и авторизации.
 */
@Slf4j
@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Открытые маршруты, доступные без аутентификации.
     */
    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

    /**
     * Настройка фильтров и правил для доступа к маршрутам, а также обработка
     * ошибок аутентификации и авторизации.
     *
     * @param http HttpSecurity для настройки базовых правил доступа.
     * @param authenticationManager Аутентификационный менеджер, необходимый для создания фильтра аутентификации.
     * @return SecurityWebFilterChain с настроенными правилами доступа и обработкой ошибок.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .pathMatchers(publicRoutes)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe , e) -> {
                    log.error("IN securityWebFilterChain - unauthorized error: {}", e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                })
                .accessDeniedHandler((swe, e) -> {
                    log.error("IN securityWebFilterChain - access denied: {}", e.getMessage());

                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                })
                .and()
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * Создает фильтр аутентификации с использованием токена Bearer для
     * аутентификации пользователей.
     *
     * @param authenticationManager Аутентификационный менеджер, используемый для проверки подлинности пользователей.
     * @return AuthenticationWebFilter с настроенными поведением аутентификации, использующим Bearer-токены.
     */
    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }
}
