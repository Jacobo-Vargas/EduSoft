package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "coverUrl", ignore = true)
    User toEntity(RequestUserDTO requestUserDTO);
    @Mapping(target = "coverUrl", ignore = true)
    ResponseUserDTO toResponseDTO(User user);
    @Mapping(target = "coverUrl", ignore = true)
    List<ResponseUserDTO> toResponseDTOList(List<User> users);

    @Named("safeMapping")
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ResponseUserDTO toSafeResponseDTO(User user);


}
