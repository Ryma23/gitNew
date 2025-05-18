package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Classe;
import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import esprit.git.gestionsummary_backgit_me.Service.AdvancedSearchService;
import esprit.git.gestionsummary_backgit_me.Service.DetailedStatistics;
import esprit.git.gestionsummary_backgit_me.Service.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/search")
public class AdvancedSearchController {

    @Autowired
    private AdvancedSearchService advancedSearchService;

    // Endpoints pour la recherche des classes avec pagination
    @GetMapping("/classes/niveau/{niveauId}/page")
    public ResponseEntity<Page<Classe>> searchClassesByNiveauPaged(
            @PathVariable Long niveauId,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchClassesByNiveau(niveauId, pagination));
    }

    @GetMapping("/classes/specialite/{specialiteId}/page")
    public ResponseEntity<Page<Classe>> searchClassesBySpecialitePaged(
            @PathVariable Long specialiteId,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchClassesBySpecialite(specialiteId, pagination));
    }

    @GetMapping("/classes/capacity/page")
    public ResponseEntity<Page<Classe>> searchClassesByCapacityPaged(
            @RequestParam int minCapacity,
            @RequestParam int maxCapacity,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchClassesByCapacity(minCapacity, maxCapacity, pagination));
    }

    @GetMapping("/classes/multi/page")
    public ResponseEntity<Page<Classe>> searchClassesMultiCriteriaPaged(
            @RequestParam(required = false) Long niveauId,
            @RequestParam(required = false) Long specialiteId,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer maxCapacity,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchClassesMultiCriteria(niveauId, specialiteId, minCapacity, maxCapacity, pagination));
    }

    // Endpoints pour la recherche des niveaux avec pagination
    @GetMapping("/niveaux/specialite/{specialiteId}/page")
    public ResponseEntity<Page<Niveau>> searchNiveauxBySpecialitePaged(
            @PathVariable Long specialiteId,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchNiveauxBySpecialite(specialiteId, pagination));
    }

    @GetMapping("/niveaux/annee/{annee}/page")
    public ResponseEntity<Page<Niveau>> searchNiveauxByAnneePaged(
            @PathVariable String annee,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchNiveauxByAnnee(annee, pagination));
    }

    // Endpoints pour la recherche des spécialités avec pagination
    @GetMapping("/specialites/type/{typeFormation}/page")
    public ResponseEntity<Page<Specialite>> searchSpecialitesByTypeFormationPaged(
            @PathVariable String typeFormation,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchSpecialitesByTypeFormation(typeFormation, pagination));
    }

    @GetMapping("/specialites/niveau/{niveauId}/page")
    public ResponseEntity<Page<Specialite>> searchSpecialitesByNiveauPaged(
            @PathVariable Long niveauId,
            @ModelAttribute PaginationRequest pagination) {
        return ResponseEntity.ok(advancedSearchService.searchSpecialitesByNiveau(niveauId, pagination));
    }

    // Endpoints pour les statistiques détaillées
    @GetMapping("/stats/detailed")
    public ResponseEntity<DetailedStatistics> getDetailedStatistics() {
        return ResponseEntity.ok(advancedSearchService.getDetailedStatistics());
    }

    @GetMapping("/stats/detailed/niveau/{niveauId}")
    public ResponseEntity<DetailedStatistics> getDetailedStatisticsByNiveau(@PathVariable Long niveauId) {
        return ResponseEntity.ok(advancedSearchService.getDetailedStatisticsByNiveau(niveauId));
    }

    @GetMapping("/stats/detailed/specialite/{specialiteId}")
    public ResponseEntity<DetailedStatistics> getDetailedStatisticsBySpecialite(@PathVariable Long specialiteId) {
        return ResponseEntity.ok(advancedSearchService.getDetailedStatisticsBySpecialite(specialiteId));
    }


} 