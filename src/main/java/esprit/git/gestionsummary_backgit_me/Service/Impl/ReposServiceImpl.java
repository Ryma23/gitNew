package esprit.git.gestionsummary_backgit_me.Service.Impl;

import esprit.git.gestionsummary_backgit_me.Entities.Repos;
import esprit.git.gestionsummary_backgit_me.Repositories.ReposRepository;
import esprit.git.gestionsummary_backgit_me.Service.ReposService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReposServiceImpl implements ReposService {

    @Autowired
    private ReposRepository reposRepository;

    @Override
    public Repos ajoutRepos(Repos repos) {
        return reposRepository.save(repos);
    }

    @Override
    public Repos modifierRepos(Repos repos) {
        return reposRepository.save(repos);
    }

    @Override
    public void deleteRepos(Long id) {
        reposRepository.deleteById(id);
    }

    @Override
    public List<Repos> lireRepos() {
        return reposRepository.findAll();
    }

    @Override
    public List<Repos> lireReposByClasse(Long classeId) {
        return reposRepository.findByClasseId(classeId);
    }

    @Override
    public List<Repos> lireReposByEleve(Long eleveId) {
        return reposRepository.findByEleveId(eleveId);
    }

    @Override
    public Page<Repos> getReposByClasse(Long classeId, Pageable pageable) {
        return reposRepository.findByClasseId(classeId, pageable);
    }

    @Override
    public Optional<Repos> getReposById(Long id) {
        return reposRepository.findById(id);
    }

    @Override
    public List<Repos> getReposByDateRange(Long classeId, LocalDateTime startDate, LocalDateTime endDate) {
        return reposRepository.findByClasseIdAndDateRange(classeId, startDate, endDate);
    }

    @Override
    public List<Repos> getReposByStatut(Long classeId, String statut) {
        return reposRepository.findByClasseIdAndStatut(classeId, statut);
    }

    @Override
    public void updateReposStatut(Long reposId, String statut) {
        Optional<Repos> reposOpt = reposRepository.findById(reposId);
        if (reposOpt.isPresent()) {
            Repos repos = reposOpt.get();
            repos.setStatut(statut);
            reposRepository.save(repos);
        }
    }

    @Override
    public byte[] exportReposToExcel(Long classeId) throws IOException {
        List<Repos> repos = reposRepository.findByClasseId(classeId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repos");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Étudiant");
        headerRow.createCell(1).setCellValue("Date Début");
        headerRow.createCell(2).setCellValue("Date Fin");
        headerRow.createCell(3).setCellValue("Motif");
        headerRow.createCell(4).setCellValue("Statut");

        // Fill data
        int rowNum = 1;
        for (Repos r : repos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getEleve().getNom() + " " + r.getEleve().getPrenom());
            row.createCell(1).setCellValue(r.getDateDebut().toString());
            row.createCell(2).setCellValue(r.getDateFin().toString());
            row.createCell(3).setCellValue(r.getMotif());
            row.createCell(4).setCellValue(r.getStatut());
        }

        // Auto-size columns
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
} 