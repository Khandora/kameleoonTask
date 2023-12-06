package com.kameleoon.test.controller;

import com.kameleoon.test.model.dto.QuoteDto;
import com.kameleoon.test.model.entity.Quote;
import com.kameleoon.test.model.entity.Vote;
import com.kameleoon.test.service.QuoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quote")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public Quote create(@RequestBody QuoteDto quoteDto) {
        return quoteService.create(quoteDto);
    }

    @GetMapping
    public List<Quote> readAll() {
        return quoteService.readAll();
    }

    @GetMapping("/{id}")
    public Quote readById(@PathVariable Long id) {
        return quoteService.readById(id);
    }

    @GetMapping("/random")
    public Quote getRandom() {
        return quoteService.getRandom();
    }

    @GetMapping("page/{page}")
    public Page<Quote> readAllPage(@PathVariable int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sort,
                                   @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return quoteService.readAllPage(page, size, sort, direction);
    }

    @GetMapping("/top/{limit}")
    public List<Quote> getTopQuotes(@PathVariable int limit) {
        return quoteService.getTopQuotes(limit);
    }

    @GetMapping("/worst/{limit}")
    public List<Quote> getWorstQuotes(@PathVariable int limit) {
        return quoteService.getWorstQuotes(limit);
    }

    @PutMapping("/{id}")
    public Quote update(@PathVariable Long id, @RequestBody Quote quote) {
        return quoteService.update(id, quote);
    }

    @PostMapping("/{id}/upvote")
    public Vote upvote(@PathVariable Long id) {
        return quoteService.upvote(id);
    }

    @PostMapping("/{id}/downvote")
    public Vote downvote(@PathVariable Long id) {
        return quoteService.downvote(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        quoteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
