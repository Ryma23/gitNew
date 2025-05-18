package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import esprit.git.gestionsummary_backgit_me.Repositories.SpecialiteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialiteService {
    private static final Logger logger = LoggerFactory.getLogger(SpecialiteService.class);

    private final SpecialiteRepository specialiteRepository;

    @Autowired
    public SpecialiteService(SpecialiteRepository specialiteRepository) {
        this.specialiteRepository = specialiteRepository;
    }

    public Page<Specialite> getAllSpecialites(Pageable pageable) {
        logger.info("Getting all specialites with pagination: {}", pageable);
        try {
            return specialiteRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error getting all specialites", e);
            throw new RuntimeException("Error getting all specialites: " + e.getMessage());
        }
    }

    public Page<Specialite> searchSpecialites(String searchTerm, Pageable pageable) {
        logger.info("Searching specialites with term: {} and pagination: {}", searchTerm, pageable);
        try {
            return specialiteRepository.findBySpecialitesContainingIgnoreCase(searchTerm, pageable);
        } catch (Exception e) {
            logger.error("Error searching specialites", e);
            throw new RuntimeException("Error searching specialites: " + e.getMessage());
        }
    }

    public Optional<Specialite> getSpecialiteById(Long id) {
        logger.info("Getting specialite by id: {}", id);
        try {
            return specialiteRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error getting specialite by id", e);
            throw new RuntimeException("Error getting specialite by id: " + e.getMessage());
        }
    }

    public Specialite saveSpecialite(Specialite specialite) {
        logger.info("Saving specialite: {}", specialite);
        try {
            return specialiteRepository.save(specialite);
        } catch (Exception e) {
            logger.error("Error saving specialite", e);
            throw new RuntimeException("Error saving specialite: " + e.getMessage());
        }
    }

    public void deleteSpecialite(Long id) {
        logger.info("Deleting specialite with id: {}", id);
        try {
            specialiteRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting specialite", e);
            throw new RuntimeException("Error deleting specialite: " + e.getMessage());
        }
    }

    public ByteArrayOutputStream exportToExcel() throws IOException {
        List<Specialite> specialites = specialiteRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Spécialités");
            
            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nom");
            headerRow.createCell(2).setCellValue("Type de Formation");
            headerRow.createCell(3).setCellValue("Nombre de Niveaux");
            
            // Remplir les données
            int rowNum = 1;
            for (Specialite specialite : specialites) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(specialite.getId());
                row.createCell(1).setCellValue(specialite.getSpecialites().toString());
                row.createCell(2).setCellValue(specialite.getTypeFormation().toString());
                row.createCell(3).setCellValue(specialite.getNiveaux() != null ? specialite.getNiveaux().size() : 0);
            }
            
            // Ajuster la largeur des colonnes
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;
        }
    }

    public Specialite createSpecialite(Specialite specialite) {
        logger.info("Creating new specialite: {}", specialite);
        try {
            return specialiteRepository.save(specialite);
        } catch (Exception e) {
            logger.error("Error creating specialite", e);
            throw new RuntimeException("Error creating specialite: " + e.getMessage());
        }
    }

    public Specialite updateSpecialite(Long id, Specialite specialite) {
        logger.info("Updating specialite with id {}: {}", id, specialite);
        try {
            return specialiteRepository.findById(id)
                .map(existingSpecialite -> {
                    specialite.setId(id);
                    return specialiteRepository.save(specialite);
                })
                .orElseThrow(() -> new RuntimeException("Specialite not found with id: " + id));
        } catch (Exception e) {
            logger.error("Error updating specialite", e);
            throw new RuntimeException("Error updating specialite: " + e.getMessage());
        }
    }
} 