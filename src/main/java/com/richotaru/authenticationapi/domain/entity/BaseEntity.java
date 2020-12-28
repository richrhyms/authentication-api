package com.richotaru.authenticationapi.domain.entity;

import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import lombok.Data;

import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Named
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public Timestamp dateCreated;
    public Timestamp lastUpdated;
    @Enumerated(EnumType. STRING)
    public GenericStatusConstant status;
}
