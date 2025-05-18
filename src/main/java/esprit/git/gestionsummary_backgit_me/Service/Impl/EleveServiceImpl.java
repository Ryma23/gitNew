package esprit.git.gestionsummary_backgit_me.Service.Impl;

import esprit.git.gestionsummary_backgit_me.Entities.Classe;
import esprit.git.gestionsummary_backgit_me.Entities.Eleve;
import esprit.git.gestionsummary_backgit_me.Repositories.ClasseRepository;
import esprit.git.gestionsummary_backgit_me.Repositories.EleveRepository;
import esprit.git.gestionsummary_backgit_me.Service.EleveService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EleveServiceImpl implements EleveService {

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Override
    public Eleve ajoutEleve(Eleve eleve) {
        return eleveRepository.save(eleve);
    }

    @Override
    public Eleve modifierEleve(Eleve eleve) {
        return eleveRepository.save(eleve);
    }

    @Override
    public void deleteEleve(Long id) {
        eleveRepository.deleteById(id);
    }

    @Override
    public List<Eleve> lireEleves() {
        return eleveRepository.findAll();
    }

    @Override
    public List<Eleve> lireElevesByClasse(Long classeId) {
        return eleveRepository.findByClasseId(classeId);
    }

    @Override
    public Page<Eleve> getElevesByClasse(Long classeId, Pageable pageable) {
        return eleveRepository.findByClasseId(classeId, pageable);
    }

    @Override
    public Page<Eleve> searchEleves(Long classeId, String searchTerm, Pageable pageable) {
        return eleveRepository.findByClasseIdAndSearch(classeId, searchTerm, pageable);
    }

    @Override
    public Optional<Eleve> getEleveById(Long id) {
        return eleveRepository.findById(id);
    }

    @Override
    public void importElevesFromExcel(MultipartFile file, Long classeId) throws IOException {
        Optional<Classe> classeOpt = classeRepository.findById(classeId);
        if (!classeOpt.isPresent()) {
            throw new RuntimeException("Classe non trouvée");
        }

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header

            Eleve eleve = new Eleve();
            eleve.setNom(getCellValueAsString(row.getCell(0)));
            eleve.setPrenom(getCellValueAsString(row.getCell(1)));
            eleve.setEmail(getCellValueAsString(row.getCell(2)));
            eleve.setNumeroEtudiant(getCellValueAsString(row.getCell(3)));
            eleve.setClasse(classeOpt.get());

            eleveRepository.save(eleve);
        }

        workbook.close();
    }

    @Override
    public byte[] exportElevesToExcel(Long classeId) throws IOException {
        List<Eleve> eleves = eleveRepository.findByClasseId(classeId);
        Optional<Classe> classeOpt = classeRepository.findById(classeId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Élèves");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nom");
        headerRow.createCell(1).setCellValue("Prénom");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Numéro Étudiant");

        // Fill data
        int rowNum = 1;
        for (Eleve eleve : eleves) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(eleve.getNom());
            row.createCell(1).setCellValue(eleve.getPrenom());
            row.createCell(2).setCellValue(eleve.getEmail());
            row.createCell(3).setCellValue(eleve.getNumeroEtudiant());
        }

        // Auto-size columns
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    @Override
    public byte[] exportListePresence(Long classeId) throws IOException {
        List<Eleve> eleves = eleveRepository.findByClasseId(classeId);
        Optional<Classe> classeOpt = classeRepository.findById(classeId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Liste de Présence");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Numéro Étudiant");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Prénom");
        headerRow.createCell(3).setCellValue("Signature");

        // Fill data
        int rowNum = 1;
        for (Eleve eleve : eleves) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(eleve.getNumeroEtudiant());
            row.createCell(1).setCellValue(eleve.getNom());
            row.createCell(2).setCellValue(eleve.getPrenom());
            row.createCell(3).setCellValue(""); // Empty cell for signature
        }

        // Auto-size columns
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
} 