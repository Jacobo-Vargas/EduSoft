package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RequestUserDTO requestUserDTO);

    ResponseUserDTO toResponseDTO(User user);

    List<ResponseUserDTO> toResponseDTOList(List<User> users);
}
