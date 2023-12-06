package com.kameleoon.test.model.entity;

import com.kameleoon.test.model.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@EnableJpaAuditing
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
