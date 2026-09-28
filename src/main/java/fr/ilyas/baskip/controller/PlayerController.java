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

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable int id) {
        Optional<Player> player = this.playerRepository.findById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/status/{status}")
    public List<Player> getPlayerByStatus(@PathVariable String status) {
        return this.playerRepository.findByStatus(PlayerStatus.valueOf(status.toUpperCase()));
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        if (this.playerRepository.existsByLicenseNumber(player.getLicenseNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            this.playerRepository.save(player);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PutMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        if (this.playerRepository.existsById(player.getPlayerId())) {
            this.playerRepository.save(player);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable int id) {
        if (this.playerRepository.existsById(id)) {
            this.playerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}