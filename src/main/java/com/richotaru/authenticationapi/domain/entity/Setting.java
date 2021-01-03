package com.richotaru.authenticationapi.domain.entity;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
//import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "setting")
@QueryEntity
@ToString
@EqualsAndHashCode(callSuper=false)
public class Setting implements Serializable {
    @Id
    @GeneratedValue
    public Long id;
    @Column(nullable = false)
    private String name;
    private String value;
    private String description;
}
