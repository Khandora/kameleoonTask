package com.kameleoon.test.repository;

import com.kameleoon.test.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByUserId(Long userId);
}
