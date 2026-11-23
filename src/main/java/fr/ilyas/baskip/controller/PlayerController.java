package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.dto.PlayerStatistics;
import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.service.PlayerService;
import fr.ilyas.baskip.service.PlayerStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerStatisticsService playerStatisticsService;

    public PlayerController(PlayerService playerService, PlayerStatisticsService playerStatisticsService) {
        this.playerService = playerService;
        this.playerStatisticsService = playerStatisticsService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return this.playerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable int id) {
        return new ResponseEntity<>(this.playerService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<PlayerStatistics> getPlayerStatistics(@PathVariable int id) {
        return new ResponseEntity<>(this.playerStatisticsService.getStatistics(id), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public List<Player> getPlayerByStatus(@PathVariable String status) {
        return this.playerService.findByStatus(status);
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(this.playerService.create(player), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        return new ResponseEntity<>(this.playerService.update(player), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable int id) {
        this.playerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}