package fr.ilyas.baskip.service;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchAssignment;
import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.model.PlayerStatus;
import fr.ilyas.baskip.repository.MatchAssignmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MatchAssignmentService {

    private final MatchAssignmentRepository matchAssignmentRepository;
    private final PlayerService playerService;
    private final MatchService matchService;

    public MatchAssignmentService(MatchAssignmentRepository matchAssignmentRepository, PlayerService playerService, MatchService matchService) {
        this.matchAssignmentRepository = matchAssignmentRepository;
        this.playerService = playerService;
        this.matchService = matchService;
    }

    @Transactional(readOnly = true)
    public List<MatchAssignment> findAll() {
        return this.matchAssignmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MatchAssignment findById(int id) {
        return this.matchAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur - Affectation " + id + " introuvable"));
    }

    @Transactional(readOnly = true)
    public List<MatchAssignment> findByMatchId(int matchId) {
        this.matchService.findById(matchId);
        return this.matchAssignmentRepository.findByMatch_MatchId(matchId);
    }

    @Transactional(readOnly = true)
    public List<MatchAssignment> findByPlayerId(int playerId) {
        this.playerService.findById(playerId);
        return this.matchAssignmentRepository.findByPlayer_PlayerId(playerId);
    }

    @Transactional(readOnly = true)
    public MatchAssignment findByPlayerIdAndMatchId(int playerId, int matchId) {
        return this.matchAssignmentRepository.findByPlayer_PlayerIdAndMatch_MatchId(playerId, matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur - Aucune affectation du joueur " + playerId + " au match " + matchId));
    }

    public MatchAssignment create(MatchAssignment assignment) {
        assignment.setAssignmentId(null);
        if (assignment.getPlayer() == null || assignment.getPlayer().getPlayerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le joueur doit être précisé");
        }
        if (assignment.getMatch() == null || assignment.getMatch().getMatchId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le match doit être précisé");
        }
        if (assignment.getPosition() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poste est obligatoire");
        }
        if (assignment.getIsSubstitute() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Il faut préciser si le joueur est titulaire ou remplaçant");
        }

        Player player = this.playerService.findById(assignment.getPlayer().getPlayerId());
        Match match = this.matchService.findById(assignment.getMatch().getMatchId());

        if (match.getStatus() != MatchStatus.PREPARATION) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le match doit être en préparation");
        }
        if (player.getStatus() != PlayerStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Seul un joueur actif peut être sélectionné");
        }
        if (this.matchAssignmentRepository.existsByPlayer_PlayerIdAndMatch_MatchId(player.getPlayerId(), match.getMatchId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le joueur " + player.getPlayerId() + " est déjà affecté au match " + match.getMatchId());
        }
        if (this.matchAssignmentRepository.existsByMatch_MatchIdAndPositionAndIsSubstitute(match.getMatchId(), assignment.getPosition(), assignment.getIsSubstitute())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Ce poste est déjà occupé pour ce match");
        }

        assignment.setPlayer(player);
        assignment.setMatch(match);
        assignment.setRating(null);
        return this.matchAssignmentRepository.save(assignment);
    }

    public MatchAssignment update(MatchAssignment assignment) {
        if (assignment.getAssignmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'id de l'affectation est nécessaire");
        }
        MatchAssignment existing = findById(assignment.getAssignmentId());
        if (assignment.getPosition() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le poste est obligatoire");
        }
        if (assignment.getIsSubstitute() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Il faut préciser si le joueur est titulaire ou remplaçant");
        }
        Match match = existing.getMatch();

        if (!assignment.getIsSubstitute().equals(existing.getIsSubstitute())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Un titulaire ne peut pas devenir remplaçant, ni l'inverse");
        }
        if (assignment.getPosition() != existing.getPosition()) {
            if (match.getStatus() != MatchStatus.PREPARATION) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le match doit être en préparation");
            }
            if (this.matchAssignmentRepository.existsByMatch_MatchIdAndPositionAndIsSubstituteAndAssignmentIdNot(
                    match.getMatchId(), assignment.getPosition(), assignment.getIsSubstitute(), existing.getAssignmentId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Ce poste est déjà occupé pour ce match");
            }
        }

        if (assignment.getRating() != null) {
            if (match.getStatus() != MatchStatus.FINISHED) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Un joueur ne peut être noté qu'une fois le match terminé");
            }
            if (assignment.getRating() < 0 || assignment.getRating() > 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - La note doit être comprise entre 0 et 20");
            }
        }

        existing.setPosition(assignment.getPosition());
        existing.setIsSubstitute(assignment.getIsSubstitute());
        existing.setRating(assignment.getRating());
        return this.matchAssignmentRepository.save(existing);
    }

    public void delete(int id) {
        MatchAssignment assignment = findById(id);
        if (assignment.getMatch().getStatus() != MatchStatus.PREPARATION) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erreur - Le match doit être en préparation");
        }
        this.matchAssignmentRepository.delete(assignment);
    }
}
