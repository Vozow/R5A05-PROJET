package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.MatchAssignment;
import fr.ilyas.baskip.repository.MatchAssignmentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/match-assignments")
public class MatchAssignmentController {

    private final MatchAssignmentRepository matchAssignmentRepository;

    public MatchAssignmentController(MatchAssignmentRepository  matchAssignmentRepository) {
        this.matchAssignmentRepository = matchAssignmentRepository;
    }

    @GetMapping
    public List<MatchAssignment> getAllMatchAssignment() {
        return matchAssignmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchAssignment> getMatchAssignmentById(@PathVariable int id) {
        Optional<MatchAssignment> matchAssignment = this.matchAssignmentRepository.findById(id);
        return matchAssignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/match/{id}")
    public List<MatchAssignment> getMatchAssignmentByMatchId(@PathVariable int id) {
        return matchAssignmentRepository.findByMatch_MatchId(id);
    }

    @GetMapping("/player/{id}")
    public List<MatchAssignment> getMatchAssignmentByPlayerId(@PathVariable int id) {
        return matchAssignmentRepository.findByPlayer_PlayerId(id);
    }

    @GetMapping("/match-player/{playerId}/{matchId}")
    public ResponseEntity<MatchAssignment> getMatchAssignmentByPlayerIdAndMatchId(@PathVariable int playerId, @PathVariable int matchId) {
        Optional<MatchAssignment> matchAssignment = this.matchAssignmentRepository.findByPlayer_PlayerIdAndMatch_MatchId(playerId, matchId);
        return matchAssignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<MatchAssignment> createMatchAssignment(@RequestBody MatchAssignment matchAssignment) {
        this.matchAssignmentRepository.save(matchAssignment);
        return new ResponseEntity<>(matchAssignment, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MatchAssignment> updateMatchAssignment(@RequestBody MatchAssignment matchAssignment) {
        if (this.matchAssignmentRepository.existsById(matchAssignment.getAssignmentId())) {
            this.matchAssignmentRepository.save(matchAssignment);
            return new ResponseEntity<>(matchAssignment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchAssignment(@PathVariable int id) {
        if (this.matchAssignmentRepository.existsById(id)) {
            this.matchAssignmentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}