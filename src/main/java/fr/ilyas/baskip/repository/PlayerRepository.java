package fr.ilyas.baskip.repository;

import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.model.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findByStatus(PlayerStatus status);

    boolean existsByLicenseNumber(Integer licenseNumber);
}