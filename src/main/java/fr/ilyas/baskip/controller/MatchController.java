package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return this.matchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable int id) {
        return new ResponseEntity<>(this.matchService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public List<Match> getMatchByStatus(@PathVariable String status) {
        return this.matchService.findByStatus(status);
    }

    @GetMapping("/before/{timestamp}")
    public List<Match> getMatchByMatchDateBefore(@PathVariable long timestamp) {
        return this.matchService.findBefore(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC));
    }

    @GetMapping("/after/{timestamp}")
    public List<Match> getMatchByMatchDateAfter(@PathVariable long timestamp) {
        return this.matchService.findAfter(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC));
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        return new ResponseEntity<>(this.matchService.create(match), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody Match match) {
        return new ResponseEntity<>(this.matchService.update(match), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable int id) {
        this.matchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
