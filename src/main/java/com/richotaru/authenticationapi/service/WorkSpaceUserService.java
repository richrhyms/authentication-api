package com.richotaru.authenticationapi.service;

import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserPojo;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;


public interface WorkSpaceUserService {
    WorkSpaceUserPojo createWorkSpaceUser(WorkSpaceUserDto dto);
    WorkSpaceUser getWorkSpaceUserByEmail(String username);
    WorkSpaceUser getWorkSpaceUserByUsername(String username);
    WorkSpaceUserAuthPojo authenticate(AccountAuthDto dto);
    WorkSpaceUserPojo getAuthenticatedUser(String username, String workSpaceCode);
}
