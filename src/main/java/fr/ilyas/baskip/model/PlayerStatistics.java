package fr.ilyas.baskip.model;

public class PlayerStatistics {

    private Integer starts; // nombre de titularisation
    private Integer substitutions; // nombre de remplacement
    private Double averageRating; // moyenne des notes
    private PlayerPosition bestPosition; // meilleur poste
    private Integer totalMatches; // nombre match
    private Integer wins; // nombre victoire
    private Integer losses; // nombre defaite
    private Double winRate; // pourcentage victoire/defaite
    private Integer consecutiveSelections; // nombre de selections consecutives

    public PlayerStatistics() {
    }

    public PlayerStatistics(Integer starts, Integer substitutions, Double averageRating, PlayerPosition bestPosition, Integer totalMatches, Integer wins, Integer losses, Double winRate, Integer consecutiveSelections) {
        this.starts = starts;
        this.substitutions = substitutions;
        this.averageRating = averageRating;
        this.bestPosition = bestPosition;
        this.totalMatches = totalMatches;
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
        this.consecutiveSelections = consecutiveSelections;
    }

    public Integer getStarts() {
        return starts;
    }

    public void setStarts(Integer starts) {
        this.starts = starts;
    }

    public Integer getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(Integer substitutions) {
        this.substitutions = substitutions;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public PlayerPosition getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(PlayerPosition bestPosition) {
        this.bestPosition = bestPosition;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Integer getConsecutiveSelections() {
        return consecutiveSelections;
    }

    public void setConsecutiveSelections(Integer consecutiveSelections) {
        this.consecutiveSelections = consecutiveSelections;
    }
}