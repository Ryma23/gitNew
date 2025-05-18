package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import esprit.git.gestionsummary_backgit_me.Repositories.NiveauRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class NiveauService {
    private static final Logger logger = LoggerFactory.getLogger(NiveauService.class);

    private final NiveauRepository niveauRepository;

    @Autowired
    public NiveauService(NiveauRepository niveauRepository) {
        this.niveauRepository = niveauRepository;
    }

    public Page<Niveau> getNiveauxBySpecialite(Long specialiteId, Pageable pageable) {
        logger.info("Getting niveaux for specialite: {} with pagination: {}", specialiteId, pageable);
        try {
            // Ajouter le tri par année par défaut si non spécifié
            if (!pageable.getSort().isSorted()) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                    Sort.by(Sort.Direction.ASC, "annee"));
            }
            return niveauRepository.findBySpecialiteId(specialiteId, pageable);
        } catch (Exception e) {
            logger.error("Error getting niveaux by specialite", e);
            throw new RuntimeException("Error getting niveaux by specialite: " + e.getMessage());
        }
    }

    public Page<Niveau> searchNiveaux(Long specialiteId, String search, Pageable pageable) {
        logger.info("Searching niveaux for specialite: {} with term: {} and pagination: {}", specialiteId, search, pageable);
        try {
            // Ajouter le tri par année par défaut si non spécifié
            if (!pageable.getSort().isSorted()) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                    Sort.by(Sort.Direction.ASC, "annee"));
            }
            return niveauRepository.findBySpecialiteIdAndAnneeContaining(specialiteId, search, pageable);
        } catch (Exception e) {
            logger.error("Error searching niveaux", e);
            throw new RuntimeException("Error searching niveaux: " + e.getMessage());
        }
    }

    public Optional<Niveau> getNiveauById(Long id) {
        logger.info("Getting niveau by id: {}", id);
        try {
            return niveauRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error getting niveau by id", e);
            throw new RuntimeException("Error getting niveau by id: " + e.getMessage());
        }
    }

    public Niveau saveNiveau(Niveau niveau) {
        logger.info("Saving niveau: {}", niveau);
        try {
            // Vérifier si un niveau avec la même année existe déjà pour cette spécialité
            if (niveau.getId() == null) { // Nouveau niveau
                List<Niveau> existingNiveaux = niveauRepository.findBySpecialiteId(niveau.getSpecialite().getId());
                for (Niveau existingNiveau : existingNiveaux) {
                    if (existingNiveau.getAnnee().equals(niveau.getAnnee())) {
                        throw new RuntimeException("Un niveau avec cette année existe déjà pour cette spécialité");
                    }
                }
            } else { // Modification d'un niveau existant
                Optional<Niveau> existingNiveau = niveauRepository.findById(niveau.getId());
                if (existingNiveau.isPresent()) {
                    Niveau currentNiveau = existingNiveau.get();
                    if (!currentNiveau.getAnnee().equals(niveau.getAnnee())) {
                        List<Niveau> otherNiveaux = niveauRepository.findBySpecialiteId(niveau.getSpecialite().getId());
                        for (Niveau otherNiveau : otherNiveaux) {
                            if (!otherNiveau.getId().equals(niveau.getId()) && 
                                otherNiveau.getAnnee().equals(niveau.getAnnee())) {
                                throw new RuntimeException("Un niveau avec cette année existe déjà pour cette spécialité");
                            }
                        }
                    }
                }
            }
            return niveauRepository.save(niveau);
        } catch (Exception e) {
            logger.error("Error saving niveau", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteNiveau(Long id) {
        logger.info("Deleting niveau with id: {}", id);
        try {
            niveauRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting niveau", e);
            throw new RuntimeException("Error deleting niveau: " + e.getMessage());
        }
    }

    public byte[] exportNiveauxToExcel(Long specialiteId) {
        List<Niveau> niveaux = niveauRepository.findBySpecialiteId(specialiteId);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Niveaux");
            
            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Année");
            headerRow.createCell(2).setCellValue("Spécialité");
            
            // Remplir les données
            int rowNum = 1;
            for (Niveau niveau : niveaux) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(niveau.getId());
                row.createCell(1).setCellValue(niveau.getAnnee().toString());
                row.createCell(2).setCellValue(niveau.getSpecialite().getNom());
            }
            
            // Ajuster la largeur des colonnes
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Écrire dans un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'export Excel", e);
        }
    }
} 