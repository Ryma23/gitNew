package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Eleve;
import esprit.git.gestionsummary_backgit_me.Service.EleveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@CrossOrigin(origins = "http://localhost:4200")
public class EleveController {

    @Autowired
    private EleveService eleveService;

    @PostMapping
    public ResponseEntity<Eleve> createEleve(@RequestBody Eleve eleve) {
        return ResponseEntity.ok(eleveService.ajoutEleve(eleve));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Eleve> updateEleve(@PathVariable Long id, @RequestBody Eleve eleve) {
        eleve.setId(id);
        return ResponseEntity.ok(eleveService.modifierEleve(eleve));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEleve(@PathVariable Long id) {
        eleveService.deleteEleve(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Eleve>> getAllEleves() {
        return ResponseEntity.ok(eleveService.lireEleves());
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<Eleve>> getElevesByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(eleveService.lireElevesByClasse(classeId));
    }

    @GetMapping("/classe/{classeId}/page")
    public ResponseEntity<Page<Eleve>> getElevesByClassePage(
            @PathVariable Long classeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eleveService.getElevesByClasse(classeId, pageable));
    }

    @GetMapping("/classe/{classeId}/search")
    public ResponseEntity<Page<Eleve>> searchEleves(
            @PathVariable Long classeId,
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eleveService.searchEleves(classeId, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eleve> getEleveById(@PathVariable Long id) {
        return eleveService.getEleveById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/import/{classeId}")
    public ResponseEntity<Void> importEleves(
            @PathVariable Long classeId,
            @RequestParam("file") MultipartFile file) {
        try {
            eleveService.importElevesFromExcel(file, classeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/export/{classeId}")
    public ResponseEntity<ByteArrayResource> exportEleves(@PathVariable Long classeId) {
        try {
            byte[] excelFile = eleveService.exportElevesToExcel(classeId);
            ByteArrayResource resource = new ByteArrayResource(excelFile);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=eleves.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelFile.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/presence/{classeId}")
    public ResponseEntity<ByteArrayResource> exportListePresence(@PathVariable Long classeId) {
        try {
            byte[] excelFile = eleveService.exportListePresence(classeId);
            ByteArrayResource resource = new ByteArrayResource(excelFile);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=liste_presence.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelFile.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 