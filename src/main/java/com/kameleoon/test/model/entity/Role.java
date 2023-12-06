package com.kameleoon.test.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "Role{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
