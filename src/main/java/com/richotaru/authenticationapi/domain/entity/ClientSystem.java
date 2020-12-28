package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name="client_system")
//@Table
@QueryEntity
@EqualsAndHashCode(callSuper=false)
public class ClientSystem extends BaseEntity {
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String clientKey;
    @Column(nullable = false)
    private String displayName;
    private Timestamp dateRegistered;
    @OneToMany(targetEntity = PortalUser.class)
    private List<PortalUser> users = new ArrayList<>();
}