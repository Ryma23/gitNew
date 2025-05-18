package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import esprit.git.gestionsummary_backgit_me.Service.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/niveaux")
@CrossOrigin(origins = "http://localhost:4200")
public class NiveauController {

    @Autowired
    private NiveauService niveauService;

    @GetMapping("/specialite/{specialiteId}")
    public ResponseEntity<Page<Niveau>> getNiveauxBySpecialite(@PathVariable Long specialiteId, Pageable pageable) {
        Page<Niveau> niveaux = niveauService.getNiveauxBySpecialite(specialiteId, pageable);
        return ResponseEntity.ok(niveaux);
    }

    @GetMapping("/specialite/{specialiteId}/search")
    public ResponseEntity<Page<Niveau>> searchNiveaux(@PathVariable Long specialiteId, @RequestParam String search, Pageable pageable) {
        Page<Niveau> niveaux = niveauService.searchNiveaux(specialiteId, search, pageable);
        return ResponseEntity.ok(niveaux);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable Long id) {
        return niveauService.getNiveauById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Niveau> createNiveau(@RequestBody Niveau niveau) {
        Niveau savedNiveau = niveauService.saveNiveau(niveau);
        return ResponseEntity.ok(savedNiveau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable Long id, @RequestBody Niveau niveau) {
        return niveauService.getNiveauById(id)
                .map(existingNiveau -> {
                    niveau.setId(id);
                    return ResponseEntity.ok(niveauService.saveNiveau(niveau));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        niveauService.deleteNiveau(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/specialite/{specialiteId}/export")
    public ResponseEntity<byte[]> exportNiveauxToExcel(@PathVariable Long specialiteId) {
        byte[] excelFile = niveauService.exportNiveauxToExcel(specialiteId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "niveaux.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }
} 