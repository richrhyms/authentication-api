package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity(name="client_user")
//@Table
@QueryEntity
@EqualsAndHashCode(callSuper=false)
public class ClientUser extends BaseEntity {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String accountCode;
    @OneToOne(fetch = FetchType.EAGER,targetEntity = PortalAccount.class, mappedBy = "id")
    private PortalAccount portalAccount;
    @OneToOne(fetch = FetchType.EAGER,targetEntity = ClientSystem.class, mappedBy = "id")
    private ClientSystem clientSystem;
}
