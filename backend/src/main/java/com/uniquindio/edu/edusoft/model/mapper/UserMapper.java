package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.UpdateUserDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toDocumentCreate(CreateUserDTO userDTO);

    User toDocumentUpdate(UpdateUserDTO updateUserDto);

    CreateUserDTO toDTO(User user);

    ResponseUserDTO toDtoResponseUser(User user);

    List<ResponseUserDTO>toDTOListReponseUser(List<User> users);

}
