package esprit.git.gestionsummary_backgit_me.Controller;

import esprit.git.gestionsummary_backgit_me.Service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/classes")
    public ResponseEntity<Resource> exportClasses() throws IOException {
        Resource resource = excelExportService.exportClassesToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=classes.xlsx")
                .body(resource);
    }

    @GetMapping("/niveaux")
    public ResponseEntity<Resource> exportNiveaux() throws IOException {
        Resource resource = excelExportService.exportNiveauxToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=niveaux.xlsx")
                .body(resource);
    }

    @GetMapping("/specialites")
    public ResponseEntity<Resource> exportSpecialites() throws IOException {
        Resource resource = excelExportService.exportSpecialitesToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=specialites.xlsx")
                .body(resource);
    }

    @GetMapping("/summary")
    public ResponseEntity<Resource> exportCompleteSummary() throws IOException {
        Resource resource = excelExportService.exportCompleteSummaryToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=summary_complet.xlsx")
                .body(resource);
    }
} 