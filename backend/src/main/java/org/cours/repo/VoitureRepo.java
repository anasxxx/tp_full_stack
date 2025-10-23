package org.cours.repo;

import java.util.List;

import org.cours.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin(origins = "*")
public interface VoitureRepo extends JpaRepository<Voiture, Long> {

    List<Voiture> findByMarque(@Param("marque") String marque);

    List<Voiture> findByCouleur(@Param("couleur") String couleur);

    List<Voiture> findByMarqueAndModele(@Param("marque") String marque, @Param("modele") String modele);

    List<Voiture> findByMarqueContainingIgnoreCase(@Param("marque") String marque);

    List<Voiture> findByCouleurContainingIgnoreCase(@Param("couleur") String couleur);

    @Query("select v from Voiture v where v.prix between :min and :max")
    List<Voiture> findByPrixBetween(@Param("min") int min, @Param("max") int max);

    @RestResource(path = "byKeyword", rel = "byKeyword")
    @Query("select v from Voiture v where lower(v.marque) like lower(concat('%', :term, '%')) "
            + "or lower(v.modele) like lower(concat('%', :term, '%')) "
            + "or lower(v.couleur) like lower(concat('%', :term, '%'))")
    List<Voiture> searchByKeyword(@Param("term") String term);
}
