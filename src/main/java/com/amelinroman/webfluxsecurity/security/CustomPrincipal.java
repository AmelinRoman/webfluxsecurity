package com.amelinroman.webfluxsecurity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * @author Amelin Roman
 * Класс CustomPrincipal реализует интерфейс Principal и представляет собой кастомный
 * объект принципала, содержащий необходимые данные пользователя, такие как его ID и имя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {
    private Long id;
    private String name;

}
