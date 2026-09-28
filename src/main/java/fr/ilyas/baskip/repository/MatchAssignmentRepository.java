package fr.ilyas.baskip.repository;

import fr.ilyas.baskip.model.MatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchAssignmentRepository extends JpaRepository<MatchAssignment, Integer> {

    List<MatchAssignment> findByMatch_MatchId(Integer matchId);

    List<MatchAssignment> findByPlayer_PlayerId(Integer playerId);

    Optional<MatchAssignment> findByPlayer_PlayerIdAndMatch_MatchId(Integer playerId, Integer matchId);

    boolean existsByPlayer_PlayerId(Integer playerId);
}