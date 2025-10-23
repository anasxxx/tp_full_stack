package org.cours.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "proprietaires")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Proprietaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Voiture> voitures = new ArrayList<>();

    public Proprietaire() {
    }

    public Proprietaire(Long id, String nom, String prenom, List<Voiture> voitures) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        if (voitures != null) {
            voitures.forEach(this::addVoiture);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<Voiture> voitures) {
        this.voitures.clear();
        if (voitures != null) {
            voitures.forEach(this::addVoiture);
        }
    }

    public void addVoiture(Voiture voiture) {
        voitures.add(voiture);
        voiture.setProprietaire(this);
    }

    public void removeVoiture(Voiture voiture) {
        voitures.remove(voiture);
        voiture.setProprietaire(null);
    }
}
