package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Classe;
import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdvancedSearchService {
    // Recherche des classes avec pagination
    Page<Classe> searchClassesByNiveau(Long niveauId, PaginationRequest pagination);
    Page<Classe> searchClassesBySpecialite(Long specialiteId, PaginationRequest pagination);
    Page<Classe> searchClassesByCapacity(int minCapacity, int maxCapacity, PaginationRequest pagination);
    Page<Classe> searchClassesMultiCriteria(Long niveauId, Long specialiteId, Integer minCapacity, Integer maxCapacity, PaginationRequest pagination);

    // Recherche des niveaux avec pagination
    Page<Niveau> searchNiveauxBySpecialite(Long specialiteId, PaginationRequest pagination);
    Page<Niveau> searchNiveauxByAnnee(String annee, PaginationRequest pagination);

    // Recherche des spécialités avec pagination
    Page<Specialite> searchSpecialitesByTypeFormation(String typeFormation, PaginationRequest pagination);
    Page<Specialite> searchSpecialitesByNiveau(Long niveauId, PaginationRequest pagination);

    // Méthodes existantes sans pagination (pour la rétrocompatibilité)
    List<Classe> searchClassesByNiveau(Long niveauId);
    List<Classe> searchClassesBySpecialite(Long specialiteId);
    List<Classe> searchClassesByCapacity(int minCapacity, int maxCapacity);
    List<Classe> searchClassesMultiCriteria(Long niveauId, Long specialiteId, Integer minCapacity, Integer maxCapacity);
    List<Niveau> searchNiveauxBySpecialite(Long specialiteId);
    List<Niveau> searchNiveauxByAnnee(String annee);
    List<Specialite> searchSpecialitesByTypeFormation(String typeFormation);
    List<Specialite> searchSpecialitesByNiveau(Long niveauId);

    // Statistiques de base
    int getTotalClassesByNiveau(Long niveauId);
    int getTotalClassesBySpecialite(Long specialiteId);
    int getTotalNiveauxBySpecialite(Long specialiteId);
    int getTotalSpecialitesByTypeFormation(String typeFormation);

    // Nouvelles statistiques détaillées
    DetailedStatistics getDetailedStatistics();
    DetailedStatistics getDetailedStatisticsByNiveau(Long niveauId);
    DetailedStatistics getDetailedStatisticsBySpecialite(Long specialiteId);

    Page<Specialite> searchSpecialites(String searchTerm, Pageable pageable);
    Page<Niveau> searchNiveaux(Long specialiteId, String searchTerm, Pageable pageable);
    Page<Classe> searchClasses(Long niveauId, String searchTerm, Pageable pageable);
    
    List<Specialite> getAllSpecialites();
    List<Niveau> getNiveauxBySpecialite(Long specialiteId);
    List<Classe> getClassesByNiveau(Long niveauId);
    
    Optional<Specialite> getSpecialiteById(Long id);
    Optional<Niveau> getNiveauById(Long id);
    Optional<Classe> getClasseById(Long id);
} 