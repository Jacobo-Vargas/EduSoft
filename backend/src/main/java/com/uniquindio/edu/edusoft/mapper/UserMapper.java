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

    @Mapping(source = "documentNumber", target = "documentNumber")
    User toDocumentCreate(CreateUserDTO userDTO);

    User toDocumentUpdate(UpdateUserDto updateUserDto);

    CreateUserDTO toDTO(User user);

    ResponseUserDto toDtoResponseUser(User user);

    List<ResponseUserDto>toDTOListReponseUser(List<User> users);

}
