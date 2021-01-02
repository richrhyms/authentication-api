package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@Table(name="client_user")
@QueryEntity
@ToString
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portal_account_fk", referencedColumnName = "id")
    private PortalAccount portalAccount;
    @ManyToOne
    private ClientSystem clientSystem;
}
