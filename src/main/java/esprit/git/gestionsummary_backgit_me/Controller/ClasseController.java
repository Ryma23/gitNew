package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Classe;
import esprit.git.gestionsummary_backgit_me.Service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionsummary/api")
public class ClasseController {

    @Autowired
    private IService iservice;

    @GetMapping("/classes/niveau/{niveauId}")
    public ResponseEntity<Page<Classe>> getClassesByNiveau(
            @PathVariable Long niveauId,
            Pageable pageable) {
        return ResponseEntity.ok(iservice.getClassesByNiveau(niveauId, pageable));
    }

    @PostMapping("/classes")
    public ResponseEntity<Classe> createClasse(@RequestBody Classe classe) {
        return ResponseEntity.ok(iservice.saveClasse(classe));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<Classe> updateClasse(
            @PathVariable Long id,
            @RequestBody Classe classe) {
        classe.setId(id);
        return ResponseEntity.ok(iservice.saveClasse(classe));
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        iservice.deleteClasse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<Classe> getClasseById(@PathVariable Long id) {
        return iservice.getClasseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/classes/search")
    public ResponseEntity<Page<Classe>> searchClasses(
            @RequestParam(required = false) Long niveauId,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable) {
        return ResponseEntity.ok(iservice.searchClasses(niveauId, searchTerm, pageable));
    }
} 