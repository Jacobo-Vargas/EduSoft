package com.uniquindio.edu.edusoft.mapper;

import com.uniquindio.edu.edusoft.model.DTO.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.DTO.user.UpdateUserDto;
import com.uniquindio.edu.edusoft.model.DTO.user.respose.ResponseUserDto;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * toca revisar esto ya que se se pone todo el mundo como estudiante si no que depende del correo
     * @param userDTO
     * @return
     */
    @Mapping(target = "userType", constant = "ESTUDIANTE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    User toDocumentCreate(CreateUserDTO userDTO);

    User toDocumentUpdate(UpdateUserDto updateUserDto);

    CreateUserDTO toDTO(User user);

    ResponseUserDto toDtoResponseUser(User user);

    List<ResponseUserDto>toDTOListReponseUser(List<User> users);

}
