package fr.ilyas.baskip.repository;

import fr.ilyas.baskip.model.Comment;
import fr.ilyas.baskip.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPlayer(Player player);

    List<Comment> findByPlayer_PlayerId(Integer playerId);
}