package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.MatchAssignment;
import fr.ilyas.baskip.service.MatchAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-assignments")
public class MatchAssignmentController {

    private final MatchAssignmentService matchAssignmentService;

    public MatchAssignmentController(MatchAssignmentService matchAssignmentService) {
        this.matchAssignmentService = matchAssignmentService;
    }

    @GetMapping
    public List<MatchAssignment> getAllMatchAssignment() {
        return this.matchAssignmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchAssignment> getMatchAssignmentById(@PathVariable int id) {
        return new ResponseEntity<>(this.matchAssignmentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/match/{id}")
    public List<MatchAssignment> getMatchAssignmentByMatchId(@PathVariable int id) {
        return this.matchAssignmentService.findByMatchId(id);
    }

    @GetMapping("/player/{id}")
    public List<MatchAssignment> getMatchAssignmentByPlayerId(@PathVariable int id) {
        return this.matchAssignmentService.findByPlayerId(id);
    }

    @GetMapping("/match-player/{playerId}/{matchId}")
    public ResponseEntity<MatchAssignment> getMatchAssignmentByPlayerIdAndMatchId(@PathVariable int playerId, @PathVariable int matchId) {
        return new ResponseEntity<>(this.matchAssignmentService.findByPlayerIdAndMatchId(playerId, matchId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MatchAssignment> createMatchAssignment(@RequestBody MatchAssignment matchAssignment) {
        return new ResponseEntity<>(this.matchAssignmentService.create(matchAssignment), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MatchAssignment> updateMatchAssignment(@RequestBody MatchAssignment matchAssignment) {
        return new ResponseEntity<>(this.matchAssignmentService.update(matchAssignment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchAssignment(@PathVariable int id) {
        this.matchAssignmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
