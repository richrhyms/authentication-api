package com.richotaru.authenticationapi.service;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceCreationDto;
import com.richotaru.authenticationapi.entity.WorkSpace;

public interface WorkSpaceService {
    WorkSpace createWorkSpace(WorkSpaceCreationDto dto);
    WorkSpace updateWorkSpace(WorkSpace account);
//    PortalAccountAuthPojo authenticate(AccountAuthDto dto) throws Exception;
//    PortalAccountPojo getAuthenticatedAccount(String username) throws UsernameNotFoundException;
}
