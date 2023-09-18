package com.amelinroman.webfluxsecurity.repository;

import com.amelinroman.webfluxsecurity.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * @author Amelin Roman
 * Интерфейс UserRepository предоставляет доступ к данным пользователей в базе данных с использованием реактивной парадигмы.
 * UserRepository расширяет R2dbcRepository для работы с сущностью UserEntity и ключом типа Long.
 */
public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);
}
