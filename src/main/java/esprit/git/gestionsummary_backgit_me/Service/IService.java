package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IService {
    // Spécialités
    Page<Specialite> getAllSpecialites(Pageable pageable);
    Optional<Specialite> getSpecialiteById(Long id);
    Specialite createSpecialite(Specialite specialite);
    Specialite updateSpecialite(Long id, Specialite specialite);
    void deleteSpecialite(Long id);
    Page<Specialite> searchSpecialites(String search, Pageable pageable);

    // Niveaux
    Page<Niveau> getNiveauxBySpecialite(Long specialiteId, Pageable pageable);
    Optional<Niveau> getNiveauById(Long id);
    Niveau saveNiveau(Niveau niveau);
    void deleteNiveau(Long id);
    Page<Niveau> searchNiveaux(Long specialiteId, String search, Pageable pageable);

    // Classes
    Page<Classe> getClassesByNiveau(Long niveauId, Pageable pageable);
    Optional<Classe> getClasseById(Long id);
    Classe saveClasse(Classe classe);
    void deleteClasse(Long id);
    Page<Classe> searchClasses(Long niveauId, String searchTerm, Pageable pageable);

    // Élèves
    List<Eleve> getElevesByClasse(Long classeId);
    Eleve addEleve(Long classeId, Eleve eleve);
    Eleve updateEleve(Eleve eleve);
    void deleteEleve(Long id);

    // Repos
    List<Repos> getReposByClasse(Long classeId);
    Repos addRepos(Long classeId, Repos repos);
    Repos updateRepos(Repos repos);
    void deleteRepos(Long id);

    // Exports
    ResponseEntity<byte[]> exportEleves(Long classeId);
    ResponseEntity<byte[]> exportRepos(Long classeId);
    ResponseEntity<byte[]> exportPresence(Long classeId);

    // Statistiques
    DetailedStatistics getDetailedStatistics();
    DetailedStatistics getDetailedStatisticsByNiveau(Long niveauId);
    DetailedStatistics getDetailedStatisticsBySpecialite(Long specialiteId);
}
