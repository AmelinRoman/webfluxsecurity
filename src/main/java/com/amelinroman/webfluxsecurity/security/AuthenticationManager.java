package com.amelinroman.webfluxsecurity.security;

import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.exception.UnauthorizedException;
import com.amelinroman.webfluxsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Amelin Roman
 * Класс AuthenticationManager реализует интерфейс ReactiveAuthenticationManager
 * и отвечает за аутентификацию пользователя в контексте реактивных веб-приложений.
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private UserService userService;

    /**
     * Аутентифицирует пользователя на основе предоставленного объекта Authentication.
     * Метод получает ID пользователя из CustomPrincipal, извлекает соответствующий
     * UserEntity с помощью UserService и проверяет, включен ли пользователь.
     * Если пользователь включен, метод возвращает объект Authentication с пользовательскими данными,
     * в противном случае возникает ошибка UnauthorizedException.
     *
     * @param authentication объект Authentication, содержащий данные пользователя.
     * @return Mono<Authentication> с аутентификационной информацией пользователя.
     * @throws UnauthorizedException если пользователь отключен.
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
                .map(user -> authentication);
    }
}
