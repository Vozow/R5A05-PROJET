package fr.ilyas.baskip.controller;

import fr.ilyas.baskip.model.Comment;
import fr.ilyas.baskip.model.Player;
import fr.ilyas.baskip.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable  int id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        return comment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/player/{id}")
    public List<Comment> getCommentsByIdPlayer(@PathVariable int id) {
        return this.commentRepository.findByPlayer_PlayerId(id);
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        this.commentRepository.save(comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        if(this.commentRepository.existsById(comment.getCommentId())) {
            this.commentRepository.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable int id) {
        if(this.commentRepository.existsById(id)) {
            this.commentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
