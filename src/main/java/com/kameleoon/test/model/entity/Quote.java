package com.kameleoon.test.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "quotes")
@EntityListeners(AuditingEntityListener.class)
public class Quote extends BaseEntity {

    @Column(name = "content")
    private String content;

    @ManyToOne
    private User createdBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quote", cascade = CascadeType.ALL)
    private Set<Vote> votes;
}
