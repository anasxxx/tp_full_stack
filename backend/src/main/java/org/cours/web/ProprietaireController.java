package org.cours.web;

import java.util.List;

import org.cours.model.Proprietaire;
import org.cours.repo.ProprietaireRepo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proprietaires")
@CrossOrigin(origins = "*")
public class ProprietaireController {

    private final ProprietaireRepo proprietaireRepo;

    public ProprietaireController(ProprietaireRepo proprietaireRepo) {
        this.proprietaireRepo = proprietaireRepo;
    }

    @GetMapping
    public List<Proprietaire> getProprietaires() {
        return proprietaireRepo.findAll();
    }
}
