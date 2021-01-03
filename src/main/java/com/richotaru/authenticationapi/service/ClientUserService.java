package com.richotaru.authenticationapi.service;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface ClientUserService {
    ClientUserPojo createClientUser(ClientUserDto dto);
    ClientUser getClientUser(String username) throws UsernameNotFoundException;
}
