package com.amelinroman.webfluxsecurity.mapper;

import com.amelinroman.webfluxsecurity.dto.UserDto;
import com.amelinroman.webfluxsecurity.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);
}
