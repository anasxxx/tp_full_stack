package org.cours.web;

import java.util.List;

import org.cours.model.Voiture;
import org.cours.repo.ProprietaireRepo;
import org.cours.repo.VoitureRepo;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/voitures")
@CrossOrigin(origins = "*")
public class VoitureController {

    private final VoitureRepo voitureRepo;
    private final ProprietaireRepo proprietaireRepo;

    public VoitureController(VoitureRepo voitureRepo, ProprietaireRepo proprietaireRepo) {
        this.voitureRepo = voitureRepo;
        this.proprietaireRepo = proprietaireRepo;
    }

    @GetMapping
    public List<Voiture> getVoitures() {
        return voitureRepo.findAll();
    }

    @GetMapping("/{id}")
    public Voiture getVoiture(@PathVariable Long id) {
        return voitureRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture introuvable"));
    }

    @GetMapping("/search")
    public List<Voiture> searchVoitures(@RequestParam(name = "q") String term) {
        return voitureRepo.searchByKeyword(term);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Voiture createVoiture(@Valid @RequestBody VoitureRequest request) {
        var proprietaire = proprietaireRepo.findById(request.proprietaireId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriétaire introuvable"));

        Voiture voiture = new Voiture(null, request.marque(), request.modele(), request.couleur(), request.matricule(),
                request.immatricule(), request.prix(), proprietaire);
        return voitureRepo.save(voiture);
    }

    @PutMapping("/{id}")
    @Transactional
    public Voiture updateVoiture(@PathVariable Long id, @Valid @RequestBody VoitureRequest request) {
        Voiture voiture = voitureRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture introuvable"));

        var proprietaire = proprietaireRepo.findById(request.proprietaireId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriétaire introuvable"));

        voiture.setMarque(request.marque());
        voiture.setModele(request.modele());
        voiture.setCouleur(request.couleur());
        voiture.setMatricule(request.matricule());
        voiture.setImmatricule(request.immatricule());
        voiture.setPrix(request.prix());
        voiture.setProprietaire(proprietaire);

        return voiture;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVoiture(@PathVariable Long id) {
        if (!voitureRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture introuvable");
        }
        voitureRepo.deleteById(id);
    }

    public record VoitureRequest(
            @jakarta.validation.constraints.NotBlank String marque,
            @jakarta.validation.constraints.NotBlank String modele,
            @jakarta.validation.constraints.NotBlank String couleur,
            @jakarta.validation.constraints.NotBlank String matricule,
            @jakarta.validation.constraints.NotBlank String immatricule,
            @jakarta.validation.constraints.Min(0) int prix,
            @jakarta.validation.constraints.NotNull Long proprietaireId) {
    }
}
