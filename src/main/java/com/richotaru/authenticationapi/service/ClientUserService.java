package com.richotaru.authenticationapi.service;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface ClientUserService {
    ClientUserPojo createClientUser(ClientUserDto dto);
    ClientUserAuthPojo authenticateClientUser(ClientUserAuthDto dto) throws UsernameNotFoundException;
    ClientUserPojo getAuthenticatedClientUser(String username) throws UsernameNotFoundException;
}
