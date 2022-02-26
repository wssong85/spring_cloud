package com.example.userservice.mapper;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring")
public interface UserMapper {

  //  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//  @Mapping(target = "")
  UserEntity userDtoToEntity(UserDto userDto);

  UserDto userEntityToDto(UserEntity userEntity);
}
