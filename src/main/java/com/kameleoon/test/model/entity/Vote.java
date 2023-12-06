package com.kameleoon.test.model.entity;

import com.kameleoon.test.model.enums.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "votes")
@EntityListeners(AuditingEntityListener.class)
public class Vote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "vote_status")
    private VoteStatus voteStatus;
}
