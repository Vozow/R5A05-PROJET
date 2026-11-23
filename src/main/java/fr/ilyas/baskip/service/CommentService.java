package fr.ilyas.baskip.service;

import fr.ilyas.baskip.model.Comment;
import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PlayerService playerService;

    public CommentService(CommentRepository commentRepository, PlayerService playerService) {
        this.commentRepository = commentRepository;
        this.playerService = playerService;
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Comment findById(int id) {
        return this.commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur - Commentaire " + id + " introuvable"));
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPlayerId(int playerId) {
        this.playerService.findById(playerId);
        return this.commentRepository.findByPlayer_PlayerId(playerId);
    }

    public Comment create(Comment comment) {
        comment.setCommentId(null);
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le commentaire est obligatoire");
        }
        if (comment.getPlayer() == null || comment.getPlayer().getPlayerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le joueur doit être précisé");
        }
        Player player = this.playerService.findById(comment.getPlayer().getPlayerId());
        comment.setPlayer(player);
        return this.commentRepository.save(comment);
    }

    public Comment update(Comment comment) {
        if (comment.getCommentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - L'id du commentaire est nécessaire");
        }
        Comment existing = findById(comment.getCommentId());
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur - Le commentaire est obligatoire");
        }
        existing.setContent(comment.getContent());
        return this.commentRepository.save(existing);
    }

    public void delete(int id) {
        Comment comment = findById(id);
        this.commentRepository.delete(comment);
    }
}
