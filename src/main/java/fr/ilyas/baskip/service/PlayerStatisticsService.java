package fr.ilyas.baskip.service;

import fr.ilyas.baskip.dto.PlayerStatistics;
import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchAssignment;
import fr.ilyas.baskip.model.MatchStatus;
import fr.ilyas.baskip.model.PlayerPosition;
import fr.ilyas.baskip.repository.MatchAssignmentRepository;
import fr.ilyas.baskip.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlayerStatisticsService {

    private final PlayerService playerService;
    private final MatchAssignmentRepository matchAssignmentRepository;
    private final MatchRepository matchRepository;

    public PlayerStatisticsService(PlayerService playerService, MatchAssignmentRepository matchAssignmentRepository, MatchRepository matchRepository) {
        this.playerService = playerService;
        this.matchAssignmentRepository = matchAssignmentRepository;
        this.matchRepository = matchRepository;
    }

    public PlayerStatistics getStatistics(int playerId) {
        this.playerService.findById(playerId);

        List<MatchAssignment> played = this.matchAssignmentRepository.findByPlayer_PlayerId(playerId).stream()
                .filter(a -> a.getMatch().getStatus() == MatchStatus.FINISHED)
                .toList();

        int totalMatches = played.size();
        int starts = (int) played.stream().filter(a -> Boolean.FALSE.equals(a.getIsSubstitute())).count();
        int substitutions = (int) played.stream().filter(a -> Boolean.TRUE.equals(a.getIsSubstitute())).count();

        int wins = (int) played.stream().map(MatchAssignment::getMatch).filter(this::isWin).count();
        int losses = (int) played.stream().map(MatchAssignment::getMatch).filter(this::isLoss).count();
        double winRate = totalMatches == 0 ? 0.0 : round(wins * 100.0 / totalMatches);

        return new PlayerStatistics(
                starts,
                substitutions,
                averageRating(played),
                bestPosition(played),
                totalMatches,
                wins,
                losses,
                winRate,
                consecutiveSelections(played)
        );
    }

    private Double averageRating(List<MatchAssignment> played) {
        OptionalDouble average = played.stream()
                .map(MatchAssignment::getRating)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average();
        return average.isPresent() ? round(average.getAsDouble()) : null;
    }

    private PlayerPosition bestPosition(List<MatchAssignment> played) {
        Map<PlayerPosition, Long> winsByPosition = played.stream()
                .filter(a -> a.getPosition() != null && isWin(a.getMatch()))
                .collect(Collectors.groupingBy(MatchAssignment::getPosition, Collectors.counting()));

        return winsByPosition.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private int consecutiveSelections(List<MatchAssignment> played) {
        Set<Integer> playedMatchIds = played.stream()
                .map(a -> a.getMatch().getMatchId())
                .collect(Collectors.toSet());

        List<Match> finishedMatches = this.matchRepository.findByStatusOrderByMatchDateAsc(MatchStatus.FINISHED);

        int maxStreak = 0;
        int currentStreak = 0;
        for (Match match : finishedMatches) {
            if (playedMatchIds.contains(match.getMatchId())) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
        }
        return maxStreak;
    }

    private boolean isWin(Match match) {
        return match.getTeamScore() != null && match.getOpponentScore() != null
                && match.getTeamScore() > match.getOpponentScore();
    }

    private boolean isLoss(Match match) {
        return match.getTeamScore() != null && match.getOpponentScore() != null
                && match.getTeamScore() < match.getOpponentScore();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
