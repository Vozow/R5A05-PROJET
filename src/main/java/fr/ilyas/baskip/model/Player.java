package fr.ilyas.baskip.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDate;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerId;
    private String lastName;
    private String firstName;
    private Integer licenseNumber;
    private Double weight;
    private LocalDate birthDate;
    private Double height;

    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

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
}