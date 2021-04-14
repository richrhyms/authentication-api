package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.enumeration.WorkSpaceTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkSpaceUpdateDto {
    private WorkSpaceTypeConstant accountType;
    private String displayName;
    private GenericStatusConstant status;
}
