package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Repos;
import esprit.git.gestionsummary_backgit_me.Service.ReposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/repos")
@CrossOrigin(origins = "http://localhost:4200")
public class ReposController {

    @Autowired
    private ReposService reposService;

    @PostMapping
    public ResponseEntity<Repos> createRepos(@RequestBody Repos repos) {
        return ResponseEntity.ok(reposService.ajoutRepos(repos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repos> updateRepos(@PathVariable Long id, @RequestBody Repos repos) {
        repos.setId(id);
        return ResponseEntity.ok(reposService.modifierRepos(repos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepos(@PathVariable Long id) {
        reposService.deleteRepos(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Repos>> getAllRepos() {
        return ResponseEntity.ok(reposService.lireRepos());
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<Repos>> getReposByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(reposService.lireReposByClasse(classeId));
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<List<Repos>> getReposByEleve(@PathVariable Long eleveId) {
        return ResponseEntity.ok(reposService.lireReposByEleve(eleveId));
    }

    @GetMapping("/classe/{classeId}/page")
    public ResponseEntity<Page<Repos>> getReposByClassePage(
            @PathVariable Long classeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reposService.getReposByClasse(classeId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repos> getReposById(@PathVariable Long id) {
        return reposService.getReposById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/classe/{classeId}/date-range")
    public ResponseEntity<List<Repos>> getReposByDateRange(
            @PathVariable Long classeId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(reposService.getReposByDateRange(classeId, startDate, endDate));
    }

    @GetMapping("/classe/{classeId}/statut/{statut}")
    public ResponseEntity<List<Repos>> getReposByStatut(
            @PathVariable Long classeId,
            @PathVariable String statut) {
        return ResponseEntity.ok(reposService.getReposByStatut(classeId, statut));
    }

    @PutMapping("/{id}/statut/{statut}")
    public ResponseEntity<Void> updateReposStatut(
            @PathVariable Long id,
            @PathVariable String statut) {
        reposService.updateReposStatut(id, statut);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export/{classeId}")
    public ResponseEntity<ByteArrayResource> exportRepos(@PathVariable Long classeId) {
        try {
            byte[] excelFile = reposService.exportReposToExcel(classeId);
            ByteArrayResource resource = new ByteArrayResource(excelFile);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=repos.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelFile.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 