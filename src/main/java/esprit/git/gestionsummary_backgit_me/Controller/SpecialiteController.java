package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import esprit.git.gestionsummary_backgit_me.Service.SpecialiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/specialites")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SpecialiteController {

    private static final Logger logger = LoggerFactory.getLogger(SpecialiteController.class);

    @Autowired
    private SpecialiteService specialiteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Specialite> getAllSpecialites(Pageable pageable) {
        logger.info("GET /api/specialites - Getting all specialites with pagination: page={}, size={}", 
            pageable.getPageNumber(), pageable.getPageSize());
        return specialiteService.getAllSpecialites(pageable);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Specialite> searchSpecialites(@RequestParam String search, Pageable pageable) {
        logger.info("Searching specialites with term: {} and pagination: {}", search, pageable);
        return specialiteService.searchSpecialites(search, pageable);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialite> getSpecialiteById(@PathVariable Long id) {
        logger.info("Getting specialite by id: {}", id);
        return specialiteService.getSpecialiteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialite> createSpecialite(@RequestBody Specialite specialite) {
        logger.info("POST /api/specialites - Received request to create specialite: {}", specialite);
        try {
            if (specialite == null) {
                logger.error("POST /api/specialites - Specialite object is null");
                return ResponseEntity.badRequest().build();
            }
            
            if (specialite.getNom() == null || specialite.getNom().trim().isEmpty()) {
                logger.error("POST /api/specialites - Specialite name is null or empty");
                return ResponseEntity.badRequest().build();
            }
            
            if (specialite.getSpecialites() == null) {
                logger.error("POST /api/specialites - Specialites enum is null");
                return ResponseEntity.badRequest().build();
            }
            
            if (specialite.getTypeFormation() == null) {
                logger.error("POST /api/specialites - TypeFormation enum is null");
                return ResponseEntity.badRequest().build();
            }

            specialite.setNom(specialite.getNom().trim());
            if (specialite.getNiveaux() == null) {
                specialite.setNiveaux(new ArrayList<>());
            }

            Specialite savedSpecialite = specialiteService.createSpecialite(specialite);
            logger.info("POST /api/specialites - Successfully saved specialite with id: {}", savedSpecialite.getId());
            
            return ResponseEntity.ok(savedSpecialite);
        } catch (Exception e) {
            logger.error("POST /api/specialites - Error creating specialite: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialite> updateSpecialite(@PathVariable Long id, @RequestBody Specialite specialite) {
        logger.info("PUT /api/specialites/{} - Received request to update specialite: {}", id, specialite);
        try {
            specialite.setId(id);
            Specialite updatedSpecialite = specialiteService.updateSpecialite(id, specialite);
            logger.info("PUT /api/specialites/{} - Successfully updated specialite", id);
            return ResponseEntity.ok(updatedSpecialite);
        } catch (Exception e) {
            logger.error("PUT /api/specialites/{} - Error updating specialite: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSpecialite(@PathVariable Long id) {
        logger.info("Deleting specialite with id: {}", id);
        specialiteService.deleteSpecialite(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportToExcel() {
        logger.info("Exporting specialites to Excel");
        try {
            ByteArrayOutputStream outputStream = specialiteService.exportToExcel();
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "specialites.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            logger.error("Error exporting specialites to Excel: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 