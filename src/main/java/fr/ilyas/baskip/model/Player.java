package fr.ilyas.baskip.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerId;
    private String lastName;
    private String firstName;
    @Column(unique = true, nullable = false)
    private Integer licenseNumber;
    private Double weight;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Double height;

    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE)
    private List<MatchAssignment> assignments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Player() {
    }

    public Player(Integer playerId, String lastName, String firstName, Integer licenseNumber, Double weight, LocalDate birthDate, Double height, PlayerStatus status) {
        this.playerId = playerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.licenseNumber = licenseNumber;
        this.weight = weight;
        this.birthDate = birthDate;
        this.height = height;
        this.status = status;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Integer licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public List<MatchAssignment> getAssignments() {
        return assignments;
    }

    public List<Comment> getComments() {
        return comments;
    }

}