package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Data
@Entity(name="portal_user")
//@Table
@QueryEntity
@EqualsAndHashCode(callSuper=false)
public class PortalUser extends BaseEntity {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String dateRegistered;
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = ClientSystem.class)
    private ClientSystem clientSystem;
}
