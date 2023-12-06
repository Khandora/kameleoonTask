package com.kameleoon.test.service;

import com.kameleoon.test.exception.ResourceNotFoundException;
import com.kameleoon.test.model.dto.QuoteDto;
import com.kameleoon.test.model.entity.Quote;
import com.kameleoon.test.model.entity.User;
import com.kameleoon.test.model.entity.Vote;
import com.kameleoon.test.model.enums.VoteStatus;
import com.kameleoon.test.repository.QuoteRepository;
import com.kameleoon.test.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final VoteRepository voteRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public QuoteService(QuoteRepository quoteRepository, VoteRepository voteRepository, AuthenticatedUserProvider authenticatedUserProvider) {
        this.quoteRepository = quoteRepository;
        this.voteRepository = voteRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public Quote create(QuoteDto quoteDto) {
        User user = authenticatedUserProvider.getCurrentAuthenticatedUser();
        Quote quote = new Quote(quoteDto.getContent(), user, null);
        return quoteRepository.save(quote);
    }

    public List<Quote> readAll() {
        List<Quote> result = quoteRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    public Page<Quote> readAllPage(int page, int size, String sort, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return quoteRepository.findAll(pageable);
    }

    public Quote readById(Long id) {
        Quote result = quoteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + id));
        log.info("IN readById - quote: {} found by id: {}", result, id);

        return result;
    }

    public Quote getRandom() {
        List<Quote> quotes = quoteRepository.findAll();
        int randomIndex = new Random().nextInt(quotes.size());
        return quotes.get(randomIndex);
    }

    public List<Quote> getTopQuotes(int limit) {
        return quoteRepository.findTopByUpvotesMinusDownvotes(PageRequest.of(0, limit));
    }

    public List<Quote> getWorstQuotes(int limit) {
        return quoteRepository.findWorstByUpvotesMinusDownvotes(PageRequest.of(0, limit));
    }

    public Quote update(Long id, Quote updatedQuote) {
        Quote quote = quoteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + id));
        quote.setContent(updatedQuote.getContent());
        return quoteRepository.save(quote);
    }

    public Vote upvote(Long id) {
        Optional<Quote> quote = quoteRepository.findByIdWithVotes(id);
        if (quote.isPresent()) {
            Vote vote = getVoteFromSet(quote.get().getVotes(), id);
            if (vote != null) {
                vote.setVoteStatus(VoteStatus.UPVOTE);
                voteRepository.save(vote);
            } else {
                vote = new Vote(quote.get(), authenticatedUserProvider.getCurrentAuthenticatedUser(), VoteStatus.UPVOTE);
                voteRepository.save(vote);
            }
            quote.get().getVotes().add(vote);
            quoteRepository.save(quote.get());

            return vote;
        } else {
            throw new ResourceNotFoundException("Quote not found!");
        }
    }

    public Vote downvote(Long id) {
        Optional<Quote> quote = quoteRepository.findByIdWithVotes(id);
        if (quote.isPresent()) {
            Vote vote = getVoteFromSet(quote.get().getVotes(), id);
            if (vote != null) {
                vote.setVoteStatus(VoteStatus.DOWNVOTE);
                voteRepository.save(vote);
            } else {
                vote = new Vote(quote.get(), authenticatedUserProvider.getCurrentAuthenticatedUser(), VoteStatus.DOWNVOTE);
                voteRepository.save(vote);
            }
            quote.get().getVotes().add(vote);
            quoteRepository.save(quote.get());

            return vote;
        } else {
            throw new ResourceNotFoundException("Quote not found!");
        }
    }

    public void delete(Long id) {
        quoteRepository.deleteById(id);
        log.info("IN delete - quote with id: {} successfully deleted", id);
    }

    private Vote getVoteFromSet(Set<Vote> voteSet, Long id) {
        for (Vote vote : voteSet) {
            if (vote.getId().equals(id)) {
                return vote;
            }
        }
        return null;
    }
}
