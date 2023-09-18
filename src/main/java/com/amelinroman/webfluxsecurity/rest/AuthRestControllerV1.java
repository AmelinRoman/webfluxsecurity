package com.amelinroman.webfluxsecurity.rest;

import com.amelinroman.webfluxsecurity.dto.AuthRequestDto;
import com.amelinroman.webfluxsecurity.dto.AuthResponseDto;
import com.amelinroman.webfluxsecurity.dto.UserDto;
import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.mapper.UserMapper;
import com.amelinroman.webfluxsecurity.security.CustomPrincipal;
import com.amelinroman.webfluxsecurity.security.SecurityService;
import com.amelinroman.webfluxsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Amelin Roman
 * AuthRestControllerV1 — это контроллер REST, отвечающий за аутентификацию и регистрацию пользователей.
 * Он предоставляет конечные точки API для регистрации, входа в систему и получения информации о пользователе.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper mapper;


    /**
     * Регистрирует нового пользователя с помощью предоставленного UserDto.
     * Сопоставляет входящий UserDto с UserEntity и вызывает UserService для сохранения нового пользователя.
     * Сохраненный объект пользователя затем сопоставляется обратно с UserDto и возвращается как Mono.
     *
     * @param userDto UserDto с данными пользователя, которые необходимо зарегистрировать.
     * @return Mono<UserDto> содержащий данные вновь зарегистрированного пользователя.
     */
    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        UserEntity entity = mapper.map(userDto);
        return userService.registerUser(entity)
                .map(mapper::map);

    }

    /**
     * Аутентифицирует пользователя и возвращает AuthResponseDto с информацией для входа.
     * Вызывает SecurityService для аутентификации предоставленных учетных данных и возвращает данные.
     * Mono, содержащий AuthReponseDto с токеном и пользовательскими данными.
     *
     * @param dto AuthRequestDto, содержащий данные для входа пользователя (имя пользователя и пароль).
     * @return Mono<AuthResponseDto> с информацией об аутентификации пользователя.
     */
    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    /**
     * Получает информацию о вошедшем в систему пользователе, включая идентификатор, имя пользователя и роли.
     * Метод использует объект Authentication для получения пользовательских данных, затем вызывает UserService.
     * для получения соответствующего объекта пользователя. Затем сущность пользователя сопоставляется с UserDto.
     * и вернулся как Mono.
     *
     * @param authentication Аутентификация, полученная из контекста безопасности вошедшего в систему пользователя.
     * @return Mono<UserDto>, содержащий информацию о вошедшем в систему пользователе.
     */
    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(customPrincipal.getId())
                .map(mapper::map);
    }
}
