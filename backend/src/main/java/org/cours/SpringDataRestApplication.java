package org.cours;

import java.util.List;

import org.cours.model.Proprietaire;
import org.cours.model.Voiture;
import org.cours.repo.ProprietaireRepo;
import org.cours.repo.VoitureRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRestApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(VoitureRepo voitureRepo, ProprietaireRepo proprietaireRepo) {
        return args -> {
            voitureRepo.deleteAll();
            proprietaireRepo.deleteAll();

            Proprietaire p1 = new Proprietaire(null, "Nafil", "Khalid", null);
            Proprietaire p2 = new Proprietaire(null, "Doe", "Jane", null);
            Proprietaire p3 = new Proprietaire(null, "Dupont", "Hassan", null);

            proprietaireRepo.saveAll(List.of(p1, p2, p3));

            Voiture v1 = new Voiture(null, "Toyota", "Corolla", "Gris", "A-91-9090", "2018-95000", 95000, p1);
            Voiture v2 = new Voiture(null, "Peugeot", "206", "Bleu", "A-51-4520", "2017-87000", 87000, p1);
            Voiture v3 = new Voiture(null, "Renault", "Clio", "Rouge", "B-45-1200", "2019-102000", 102000, p2);
            Voiture v4 = new Voiture(null, "Honda", "Civic", "Noir", "C-78-9900", "2020-125000", 125000, p2);
            Voiture v5 = new Voiture(null, "Hyundai", "i30", "Blanc", "D-22-4490", "2021-142000", 142000, p3);
            Voiture v6 = new Voiture(null, "Ford", "Focus", "Bleu", "E-90-2200", "2016-78000", 78000, p3);

            voitureRepo.saveAll(List.of(v1, v2, v3, v4, v5, v6));
        };
    }
}
