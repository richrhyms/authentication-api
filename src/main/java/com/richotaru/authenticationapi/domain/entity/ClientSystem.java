package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@QueryEntity
@EqualsAndHashCode(callSuper=false)
public class ClientSystem extends BaseEntity {
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String clientCode;
    @Column(nullable = false)
    private String displayName;
    @OneToOne
    private PortalAccount portalAccount;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id", targetEntity = ClientUser.class)
    private List<ClientUser> users = new ArrayList<>();
}
