package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.uniquindio.edu.edusoft.model.entities.User;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    // Mapeo normal con MapStruct
    User loginRequestToUser(LoginRequestDTO loginRequest);

    // Mapeos específicos según tipo
    default User fromEmail(LoginRequestDTO loginRequest) {
        User user = new User();
        user.setEmail(loginRequest.getUsername());
        user.setPhone(null);
        user.setPassword(loginRequest.getPassword()); // falta que se hashee
        return user;
    }

    default User fromPhone(LoginRequestDTO loginRequest) {
        User user = new User();
        user.setPhone(loginRequest.getUsername());
        user.setEmail(null);
        user.setPassword(loginRequest.getPassword()); // falta que se hashee
        return user;
    }
}