package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@QueryEntity
@EqualsAndHashCode(callSuper=false)
public class PortalAccount extends BaseEntity {
    @Column(nullable = false)
    private String accountCode;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String jwtToken;
    @Column(nullable = false)
    private String roles;
    @Column(nullable = false)
    @Enumerated(EnumType. STRING)
    public AccountTypeConstant accountType;
//    @OneToOne
//    @JoinColumn(columnDefinition = "id")
//    private ClientSystem clientSystem;
//    @OneToOne
//    @JoinColumn(columnDefinition = "id")
//    private  ClientUser clientUser;
}
