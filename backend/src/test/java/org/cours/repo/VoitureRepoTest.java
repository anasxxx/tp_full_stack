package org.cours.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.cours.model.Proprietaire;
import org.cours.model.Voiture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class VoitureRepoTest {

    @Autowired
    private VoitureRepo voitureRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPerformCrudOperationsOnVoiture() {
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.setNom("Doe");
        proprietaire.setPrenom("John");
        entityManager.persistAndFlush(proprietaire);

        Voiture voiture = new Voiture(null, "Toyota", "Corolla", "Bleu", "AA-123", "AA-123-IMM", 120000, proprietaire);
        Voiture saved = voitureRepo.save(voiture);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();

        Optional<Voiture> maybeFound = voitureRepo.findById(saved.getId());
        assertThat(maybeFound).isPresent();
        Voiture found = maybeFound.orElseThrow();
        assertThat(found.getMarque()).isEqualTo("Toyota");
        assertThat(found.getProprietaire().getNom()).isEqualTo("Doe");

        found.setCouleur("Rouge");
        voitureRepo.save(found);
        entityManager.flush();
        Voiture updated = voitureRepo.findById(saved.getId()).orElseThrow();
        assertThat(updated.getCouleur()).isEqualTo("Rouge");

        voitureRepo.deleteById(saved.getId());
        entityManager.flush();
        assertThat(voitureRepo.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldSearchByKeyword() {
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.setNom("Smith");
        proprietaire.setPrenom("Anna");
        entityManager.persistAndFlush(proprietaire);

        Voiture voiture = new Voiture(null, "MilaCar", "Uber", "Blanche", "AA-999", "IMM-999", 180000, proprietaire);
        voitureRepo.save(voiture);
        entityManager.flush();

        assertThat(voitureRepo.searchByKeyword("milacar")).hasSize(1);
        assertThat(voitureRepo.searchByKeyword("rouge")).isEmpty();
    }
}
