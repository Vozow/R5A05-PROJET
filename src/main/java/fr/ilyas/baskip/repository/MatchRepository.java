package fr.ilyas.baskip.repository;

import fr.ilyas.baskip.model.Match;
import fr.ilyas.baskip.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByMatchDateBefore(LocalDateTime date);

    List<Match> findByMatchDateAfter(LocalDateTime date);

}