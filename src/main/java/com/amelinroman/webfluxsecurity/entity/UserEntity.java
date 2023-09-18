package com.amelinroman.webfluxsecurity.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author Amelin Roman
 * Класс UserEntity представляет сущность "Пользователь" для хранения данных в таблице "users".
 * Содержит поля, соответствующие полям таблицы, а также аннотации, описывающие их характеристики и связи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("users")
public class UserEntity {

    @Id
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Маскирует пароль, подменяя его звездочками в строковых представлениях объекта класса UserEntity.
     *
     * @return маскированный пароль пользователя в виде строки "*********".
     */
    @ToString.Include(name = "password")
    private String maskPassword() {
        return "*********";
    }
}
