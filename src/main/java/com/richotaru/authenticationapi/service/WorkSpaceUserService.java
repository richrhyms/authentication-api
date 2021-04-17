package com.richotaru.authenticationapi.service;

import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserPojo;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface WorkSpaceUserService {
    WorkSpaceUserPojo createWorkSpaceUser(WorkSpaceUserDto dto);
    WorkSpaceUser getWorkSpaceUserByEmail(String username) throws UsernameNotFoundException;
    WorkSpaceUser getWorkSpaceUserByUsername(String username) throws UsernameNotFoundException;
    WorkSpaceUserAuthPojo authenticate(AccountAuthDto dto) throws Exception;
    WorkSpaceUserPojo getAuthenticatedUser(String username, String workSpaceCode) throws UsernameNotFoundException;
}
