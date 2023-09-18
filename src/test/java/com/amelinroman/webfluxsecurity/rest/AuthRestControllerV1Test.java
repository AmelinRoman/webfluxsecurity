package com.amelinroman.webfluxsecurity.rest;

import com.amelinroman.webfluxsecurity.dto.AuthRequestDto;
import com.amelinroman.webfluxsecurity.dto.AuthResponseDto;
import com.amelinroman.webfluxsecurity.dto.UserDto;
import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.mapper.UserMapper;
import com.amelinroman.webfluxsecurity.security.CustomPrincipal;
import com.amelinroman.webfluxsecurity.security.SecurityService;
import com.amelinroman.webfluxsecurity.security.TokenDetails;
import com.amelinroman.webfluxsecurity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

/**
 * @author Amelin Roman
 * Класс AuthRestControllerV1Test содержит тесты для методов класса AuthRestControllerV1,
 * который отвечает за аутентификацию и регистрацию пользователей.
 */
public class AuthRestControllerV1Test {

    @Mock
    private SecurityService securityService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private AuthRestControllerV1 authRestControllerV1;

    /**
     * Перед запуском каждого тестового метода инициализирует моки для зависимостей.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Тест проверяет работу метода register класса AuthRestControllerV1.
     * Успешная регистрация пользователя должна возвращать зарегистрированный объект UserDto.
     */
    @Test
    public void testRegister() {
        UserDto userDto = new UserDto();
        UserEntity userEntity = new UserEntity();

        when(mapper.map(userDto)).thenReturn(userEntity);
        when(userService.registerUser(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity)).thenReturn(userDto);


        Mono<UserDto> response = authRestControllerV1.register(userDto);


        StepVerifier.create(response)
                .expectNext(userDto)
                .verifyComplete();

        verify(userService, times(1)).registerUser(userEntity);
        verify(mapper, times(1)).map(userDto);
        verify(mapper, times(1)).map(userEntity);
    }

    /**
     * Тест проверяет работу метода login класса AuthRestControllerV1.
     * Успешная аутентификация пользователя должна вызывать метод authenticate у
     * securityService один раз.
     */
    @Test
    public void testLogin() {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        Authentication authentication = mock(Authentication.class);
        TokenDetails tokenDetails = new TokenDetails();

        when(securityService.authenticate(authRequestDto.getUsername(), authRequestDto.getPassword()))
                .thenReturn(Mono.just(tokenDetails));

        authRestControllerV1.login(authRequestDto);

        verify(securityService, times(1)).authenticate(authRequestDto.getUsername(), authRequestDto.getPassword());
    }

    /**
     * Тест проверяет работу метода getUserInfo класса AuthRestControllerV1.
     * Корректный запрос информации о пользователе должен вызывать метод userService
     * для получения информации об пользователе и возвращать соответствующий объект UserDto.
     */
    @Test
    public void testGetUserInfo() {
        Authentication authentication = mock(Authentication.class);
        CustomPrincipal customPrincipal = mock(CustomPrincipal.class);
        UserDto userDto = new UserDto();
        UserEntity userEntity = new UserEntity();

        when(authentication.getPrincipal()).thenReturn(customPrincipal);
        when(customPrincipal.getId()).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity)).thenReturn(userDto);

        Mono<UserDto> response = authRestControllerV1.getUserInfo(authentication);

        StepVerifier.create(response)
                .expectNext(userDto)
                .verifyComplete();

        verify(userService, times(1)).getUserById(1L);
        verify(mapper, times(1)).map(userEntity);
    }
}
