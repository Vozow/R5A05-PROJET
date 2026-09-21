package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.repository.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchRepository matchRepository;

    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping
    public ResponseEntity<Match> getMatchById(@RequestParam int id) {
        Optional<Match> match = this.matchRepository.findById(id);
        return match.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Match> getMatchByStatus(@RequestParam String status) {
        return this.matchRepository.findByStatus(MatchStatus.valueOf(status));
    }

    @GetMapping
    public List<Match> getMatchByMatchDateBefore(@RequestParam LocalDateTime date) {
        return this.matchRepository.findByMatchDateBefore(date);
    }

    @GetMapping
    public List<Match> getMatchByMatchDateAfter(LocalDateTime date) {
        return this.matchRepository.findByMatchDateAfter(date);
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        this.matchRepository.save(match);
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Match> updateMatch(@RequestBody Match match) {
        if (this.matchRepository.existsById(match.getMatchId())) {
            this.matchRepository.save(match);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(match, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Match> deleteMatch(@RequestParam int id) {
        if (this.matchRepository.existsById(id)) {
            this.matchRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
