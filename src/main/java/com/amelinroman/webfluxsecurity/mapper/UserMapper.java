package com.amelinroman.webfluxsecurity.mapper;

import com.amelinroman.webfluxsecurity.dto.UserDto;
import com.amelinroman.webfluxsecurity.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * @author Amelin Roman
 * Интерфейс UserMapper предоставляет методы для преобразования между сущностью {@code UserEntity} и {@code UserDto}.
 * Используется при работе с объектами модели и передаче данных в запросах и ответах в представлении (API).
 * Использует библиотеку MapStruct для генерации маппера при компиляции.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует объект {@code UserEntity} в объект {@code UserDto}.
     *
     * @param userEntity объект типа {@code UserEntity} для преобразования.
     * @return объект типа {@code UserDto}, соответствующий переданному объекту {@code UserEntity}.
     */
    UserDto map(UserEntity userEntity);

    /**
     * Преобразует объект {@code UserDto} в объект {@code UserEntity}.
     *
     * @param userDto объект типа {@code UserDto} для преобразования.
     * @return объект типа {@code UserEntity}, соответствующий переданному объекту {@code UserDto}.
     */
    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);
}
