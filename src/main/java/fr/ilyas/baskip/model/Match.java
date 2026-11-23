package fr.ilyas.baskip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;

    private LocalDateTime matchDate;
    private String opposingTeam;
    private Boolean isHome;

    private Integer teamScore;
    private Integer opponentScore;

    private String address;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE)
    private List<MatchAssignment> assignments = new ArrayList<>();

    public Match() {
    }

    public Match(Integer matchId, LocalDateTime matchDate, String opposingTeam, Boolean isHome, Integer teamScore, Integer opponentScore, String address, MatchStatus status) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.opposingTeam = opposingTeam;
        this.isHome = isHome;
        this.teamScore = teamScore;
        this.opponentScore = opponentScore;
        this.address = address;
        this.status = status;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public String getOpposingTeam() {
        return opposingTeam;
    }

    public void setOpposingTeam(String opposingTeam) {
        this.opposingTeam = opposingTeam;
    }

    public Boolean getIsHome() {
        return isHome;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public Integer getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(Integer teamScore) {
        this.teamScore = teamScore;
    }

    public Integer getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(Integer opponentScore) {
        this.opponentScore = opponentScore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public List<MatchAssignment> getAssignments() {
        return assignments;
    }

}