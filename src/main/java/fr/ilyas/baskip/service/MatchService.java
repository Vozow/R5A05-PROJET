package fr.ilyas.baskip.service;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.repository.MatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public List<Match> findAll() {
        return this.matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Match findById(int id) {
        return this.matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur - Match " + id + " introuvable"));
    }

    @Transactional(readOnly = true)
    public List<Match> findByStatus(String status) {
        try {
            return this.matchRepository.findByStatus(MatchStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Statut de match invalide : " + status);
        }
    }

    @Transactional(readOnly = true)
    public List<Match> findBefore(LocalDateTime date) {
        return this.matchRepository.findByMatchDateBefore(date);
    }

    @Transactional(readOnly = true)
    public List<Match> findAfter(LocalDateTime date) {
        return this.matchRepository.findByMatchDateAfter(date);
    }

    public Match create(Match match) {
        match.setMatchId(null);
        if (match.getMatchDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date du match est obligatoire");
        }
        if (match.getOpposingTeam() == null || match.getOpposingTeam().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'équipe adverse est obligatoire");
        }
        if (match.getIsHome() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le lieu de la rencontre est obligatoire");
        }
        if (match.getAddress() == null || match.getAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'adresse est obligatoire");
        }
        if (match.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'état du match est obligatoire");
        }
        if (!match.getMatchDate().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Un match ne peut pas être programmé dans le passé");
        }
        if (match.getStatus() == MatchStatus.IN_PROGRESS || match.getStatus() == MatchStatus.FINISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Un match à venir ne peut pas être en cours ou terminé");
        }
        if (match.getTeamScore() != null || match.getOpponentScore() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le score ne peut être saisi que pour un match en cours ou terminé");
        }
        return this.matchRepository.save(match);
    }

    public Match update(Match match) {
        if (match.getMatchId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'id du match est nécessaire");
        }
        Match existing = findById(match.getMatchId());
        if (match.getMatchDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date du match est obligatoire");
        }
        if (match.getOpposingTeam() == null || match.getOpposingTeam().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'équipe adverse est obligatoire");
        }
        if (match.getIsHome() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le lieu de la rencontre est obligatoire");
        }
        if (match.getAddress() == null || match.getAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'adresse est obligatoire");
        }
        if (match.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'état du match est obligatoire");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean matchPasse = existing.getMatchDate() != null && !existing.getMatchDate().isAfter(now);
        if (matchPasse && (!match.getMatchDate().equals(existing.getMatchDate())
                || !match.getOpposingTeam().equals(existing.getOpposingTeam())
                || !match.getIsHome().equals(existing.getIsHome())
                || !match.getAddress().equals(existing.getAddress()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Seuls l'état et le score d'un match passé peuvent être modifiés");
        }
        if (!matchPasse && !match.getMatchDate().isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La date du match doit être dans le futur");
        }
        if ((match.getStatus() == MatchStatus.IN_PROGRESS || match.getStatus() == MatchStatus.FINISHED) && match.getMatchDate().isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Un match à venir ne peut pas être en cours ou terminé");
        }
        if ((match.getStatus() == MatchStatus.WAITING || match.getStatus() == MatchStatus.PREPARATION)
                && (match.getTeamScore() != null || match.getOpponentScore() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le score ne peut être saisi que pour un match en cours ou terminé");
        }
        if (match.getStatus() == MatchStatus.FINISHED && (match.getTeamScore() == null || match.getOpponentScore() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Les deux scores sont obligatoires pour un match terminé");
        }
        if ((match.getTeamScore() != null && match.getTeamScore() < 0) || (match.getOpponentScore() != null && match.getOpponentScore() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Un score ne peut pas être négatif");
        }

        existing.setMatchDate(match.getMatchDate());
        existing.setOpposingTeam(match.getOpposingTeam());
        existing.setIsHome(match.getIsHome());
        existing.setTeamScore(match.getTeamScore());
        existing.setOpponentScore(match.getOpponentScore());
        existing.setAddress(match.getAddress());
        existing.setStatus(match.getStatus());
        return this.matchRepository.save(existing);
    }

    public void delete(int id) {
        Match match = findById(id);
        if (match.getMatchDate() != null && !match.getMatchDate().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le match " + id + " est passé et ne peut pas être supprimé");
        }
        this.matchRepository.delete(match);
    }
}
