package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


@Data
@NoArgsConstructor
public class WorkSpaceUserPojo {
    private String firstName;
    private String lastName;
    private String displayName;
    private String username;
    private String dateRegistered;
    private String phoneNumber;
    private String email;
    private WorkSpace workSpace;
    private WorkSpaceUser user;


    public WorkSpaceUserPojo(WorkSpaceUser user) {
        BeanUtils.copyProperties(user, this);
        this.user = user;
    }

}
