package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchAssignment;
import fr.ilyas.baskip.repository.MatchAssignmentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matchassignment")
public class MatchAssignmentController {

    private final MatchAssignmentRepository matchAssignmentRepository;

    public MatchAssignmentController(MatchAssignmentRepository  matchAssignmentRepository) {
        this.matchAssignmentRepository = matchAssignmentRepository;
    }

    @GetMapping
    public List<MatchAssignment> getAllMatchAssignment() {
        return matchAssignmentRepository.findAll();
    }

    @GetMapping
    public ResponseEntity<MatchAssignment> getMatchAssignmentById(@RequestParam int id) {
        Optional<MatchAssignment> matchAssignment = this.matchAssignmentRepository.findById(id);
        return matchAssignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<MatchAssignment> getMatchAssignmentByMatchId(Integer matchId) {
        return matchAssignmentRepository.findByMatch_MatchId(matchId);
    }

    @GetMapping
    public List<MatchAssignment> getMatchAssignmentByPlayerId(Integer playerId) {
        return matchAssignmentRepository.findByPlayer_PlayerId(playerId);
    }

    @GetMapping
    public ResponseEntity<MatchAssignment> getMatchAssignmentByPlayerIdAndMatchId(Integer playerId, Integer matchId) {
        Optional<MatchAssignment> matchAssignment = this.matchAssignmentRepository.findByPlayer_PlayerIdAndMatch_MatchId(playerId, matchId);
        return matchAssignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<MatchAssignment> createMatch(@RequestBody MatchAssignment matchAssignment) {
        this.matchAssignmentRepository.save(matchAssignment);
        return new ResponseEntity<>(matchAssignment, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<MatchAssignment> updateMatch(@RequestBody MatchAssignment matchAssignment) {
        if (this.matchAssignmentRepository.existsById(matchAssignment.getAssignmentId())) {
            this.matchAssignmentRepository.save(matchAssignment);
            return new ResponseEntity<>(matchAssignment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(matchAssignment, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<MatchAssignment> deleteMatch(@RequestParam int id) {
        if (this.matchAssignmentRepository.existsById(id)) {
            this.matchAssignmentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}