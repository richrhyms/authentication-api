package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="client_system")
@QueryEntity
@ToString
@EqualsAndHashCode(callSuper=false)
public class ClientSystem extends BaseEntity{
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String clientCode;
    @Column(nullable = false)
    private String displayName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portal_account_fk", referencedColumnName = "id")
    private PortalAccount portalAccount;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "clientSystem", targetEntity = ClientUser.class)
    private List<ClientUser> users = new ArrayList<>();
}
