package com.kameleoon.test.repository;

import com.kameleoon.test.model.entity.Quote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("SELECT q FROM Quote q LEFT JOIN q.votes v GROUP BY q.id ORDER BY SUM(CASE WHEN v.voteStatus = 'UPVOTE' THEN 1 ELSE -1 END) DESC")
    List<Quote> findTopByUpvotesMinusDownvotes(Pageable pageable);

    @Query("SELECT q FROM Quote q LEFT JOIN q.votes v GROUP BY q.id ORDER BY SUM(CASE WHEN v.voteStatus = 'UPVOTE' THEN 1 ELSE -1 END) ASC")
    List<Quote> findWorstByUpvotesMinusDownvotes(Pageable pageable);

    @Query("SELECT q FROM Quote q JOIN FETCH q.votes WHERE q.id = :id")
    Optional<Quote> findByIdWithVotes(@Param("id") Long id);
}
