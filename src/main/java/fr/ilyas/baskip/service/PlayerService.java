package fr.ilyas.baskip.service;

import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.model.PlayerStatus;
import fr.ilyas.baskip.repository.MatchAssignmentRepository;
import fr.ilyas.baskip.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final MatchAssignmentRepository matchAssignmentRepository;

    public PlayerService(PlayerRepository playerRepository, MatchAssignmentRepository matchAssignmentRepository) {
        this.playerRepository = playerRepository;
        this.matchAssignmentRepository = matchAssignmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Player> findAll() {
        return this.playerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Player findById(int id) {
        return this.playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur - Joueur " + id + " introuvable"));
    }

    @Transactional(readOnly = true)
    public List<Player> findByStatus(String status) {
        try {
            return this.playerRepository.findByStatus(PlayerStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Statut de joueur invalide : " + status);
        }
    }

    public Player create(Player player) {
        player.setPlayerId(null);
        if (player.getLastName() == null || player.getLastName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le nom est obligatoire");
        }
        if (player.getFirstName() == null || player.getFirstName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le prénom est obligatoire");
        }
        if (player.getLicenseNumber() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le numéro de licence est obligatoire");
        }
        if (player.getWeight() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poids est obligatoire");
        }
        if (player.getBirthDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date de naissance est obligatoire");
        }
        if (player.getHeight() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La taille est obligatoire");
        }
        if (player.getLicenseNumber() < 1 || player.getLicenseNumber() > 99999999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le numéro de licence doit être compris entre 1 et 99999999");
        }
        if (player.getWeight() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poids doit être positif");
        }
        if (player.getHeight() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La taille doit être positive");
        }
        if (!player.getBirthDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date de naissance doit être dans le passé");
        }
        if (player.getStatus() == null) {
            player.setStatus(PlayerStatus.ACTIVE);
        }
        if (this.playerRepository.existsByLicenseNumber(player.getLicenseNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le numéro de licence " + player.getLicenseNumber() + " est déjà utilisé");
        }
        return this.playerRepository.save(player);
    }

    public Player update(Player player) {
        if (player.getPlayerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'id du joueur est nécessaire");
        }
        Player existing = findById(player.getPlayerId());
        if (player.getLastName() == null || player.getLastName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le nom est obligatoire");
        }
        if (player.getFirstName() == null || player.getFirstName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le prénom est obligatoire");
        }
        if (player.getLicenseNumber() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le numéro de licence est obligatoire");
        }
        if (player.getWeight() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poids est obligatoire");
        }
        if (player.getBirthDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date de naissance est obligatoire");
        }
        if (player.getHeight() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La taille est obligatoire");
        }
        if (player.getLicenseNumber() < 1 || player.getLicenseNumber() > 99999999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le numéro de licence doit être compris entre 1 et 99999999");
        }
        if (player.getWeight() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poids doit être positif");
        }
        if (player.getHeight() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La taille doit être positive");
        }
        if (!player.getBirthDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date de naissance doit être dans le passé");
        }
        if (player.getStatus() == null) {
            player.setStatus(PlayerStatus.ACTIVE);
        }
        if (player.getStatus() != existing.getStatus() && player.getStatus() != PlayerStatus.ACTIVE
                && this.matchAssignmentRepository.existsByPlayer_PlayerIdAndMatch_Status(existing.getPlayerId(), MatchStatus.PREPARATION)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le joueur est sélectionné pour un match en préparation, retirez-le avant de changer son état");
        }
        if (this.playerRepository.existsByLicenseNumberAndPlayerIdNot(player.getLicenseNumber(), player.getPlayerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le numéro de licence " + player.getLicenseNumber() + " est déjà utilisé");
        }
        existing.setLastName(player.getLastName());
        existing.setFirstName(player.getFirstName());
        existing.setLicenseNumber(player.getLicenseNumber());
        existing.setWeight(player.getWeight());
        existing.setBirthDate(player.getBirthDate());
        existing.setHeight(player.getHeight());
        existing.setStatus(player.getStatus());
        return this.playerRepository.save(existing);
    }

    public void delete(int id) {
        Player player = findById(id);
        if (this.matchAssignmentRepository.existsByPlayer_PlayerId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le joueur " + id + " a déjà été affecté à un match et ne peut pas être supprimé");
        }
        this.playerRepository.delete(player);
    }
}
