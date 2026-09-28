package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.repository.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchRepository matchRepository;

    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable int id) {
        Optional<Match> match = this.matchRepository.findById(id);
        return match.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/status/{status}")
    public List<Match> getMatchByStatus(@PathVariable String status) {
        return this.matchRepository.findByStatus(MatchStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping("/before/{timestamp}")
    public List<Match> getMatchByMatchDateBefore(@PathVariable long timestamp) {
        return this.matchRepository.findByMatchDateBefore(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC));
    }

    @GetMapping("/after/{timestamp}")
    public List<Match> getMatchByMatchDateAfter(@PathVariable long timestamp) {
        return this.matchRepository.findByMatchDateAfter(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC));
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        this.matchRepository.save(match);
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody Match match) {
        if (this.matchRepository.existsById(match.getMatchId())) {
            this.matchRepository.save(match);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable int id) {
        if (this.matchRepository.existsById(id)) {
            this.matchRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
