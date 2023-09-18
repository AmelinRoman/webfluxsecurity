package com.amelinroman.webfluxsecurity.service;

import com.amelinroman.webfluxsecurity.entity.UserEntity;
import com.amelinroman.webfluxsecurity.entity.UserRole;
import com.amelinroman.webfluxsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author Amelin Roman
 * Класс-сервис UserService предоставляет методы для работы с пользователями, включая регистрацию и получение пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрирует нового пользователя и сохраняет его в базу данных.
     *
     * @param user объект UserEntity, содержащий информацию о пользователе.
     * @return Mono<UserEntity> объект созданного пользователя, сохраненного в базе данных.
     */
    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerUser - user: {} created", u);
        });
    }

    /**
     * Возвращает пользовательскую сущность, найденную по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Mono<UserEntity> объект найденного пользователя или Mono.empty(), если пользователь не найден.
     */
    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Возвращает объект пользователя, найденного по имени пользователя (username).
     *
     * @param username имя пользователя для поиска.
     * @return Mono<UserEntity> объект найденного пользователя или Mono.empty(), если пользователь не найден.
     */
    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
