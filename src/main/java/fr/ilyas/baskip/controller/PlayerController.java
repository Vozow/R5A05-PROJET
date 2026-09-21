package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.model.PlayerStatus;
import fr.ilyas.baskip.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return this.playerRepository.findAll();
    }

    @GetMapping
    public ResponseEntity<Player> getPlayerById(@RequestParam int id) {
        Optional<Player> player = this.playerRepository.findById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Player> getPlayerByStatus(@RequestParam String status) {
        return this.playerRepository.findByStatus(PlayerStatus.valueOf(status));
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        if (this.playerRepository.existsByLicenseNumber(player.getLicenseNumber())) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        } else {
            this.playerRepository.save(player);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PostMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        if (this.playerRepository.existsById(player.getPlayerId())) {
            this.playerRepository.save(player);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePlayer(@RequestParam int id) {
        if (this.playerRepository.existsById(id)) {
            this.playerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}