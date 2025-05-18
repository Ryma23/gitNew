package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Classe;
import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import esprit.git.gestionsummary_backgit_me.Repositories.ClasseRepository;
import esprit.git.gestionsummary_backgit_me.Repositories.NiveauRepository;
import esprit.git.gestionsummary_backgit_me.Repositories.SpecialiteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Override
    public Resource exportClassesToExcel() throws IOException {
        List<Classe> classes = classeRepository.findAll();
        return createExcelFile(classes, "Classes");
    }

    @Override
    public Resource exportNiveauxToExcel() throws IOException {
        List<Niveau> niveaux = niveauRepository.findAll();
        return createExcelFile(niveaux, "Niveaux");
    }

    @Override
    public Resource exportSpecialitesToExcel() throws IOException {
        List<Specialite> specialites = specialiteRepository.findAll();
        return createExcelFile(specialites, "Specialites");
    }

    @Override
    public Resource exportCompleteSummaryToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Créer les feuilles
            Sheet classesSheet = workbook.createSheet("Classes");
            Sheet niveauxSheet = workbook.createSheet("Niveaux");
            Sheet specialitesSheet = workbook.createSheet("Specialites");

            // Remplir les données
            fillClassesSheet(classesSheet);
            fillNiveauxSheet(niveauxSheet);
            fillSpecialitesSheet(specialitesSheet);

            // Écrire dans un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private Resource createExcelFile(List<?> data, String sheetName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            createHeaderRow(headerRow, data.get(0).getClass());

            // Remplir les données
            int rowNum = 1;
            for (Object item : data) {
                Row row = sheet.createRow(rowNum++);
                fillRow(row, item);
            }

            // Auto-size les colonnes
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Écrire dans un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private void createHeaderRow(Row headerRow, Class<?> clazz) {
        int cellNum = 0;
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            Cell cell = headerRow.createCell(cellNum++);
            cell.setCellValue(field.getName());
        }
    }

    private void fillRow(Row row, Object item) {
        int cellNum = 0;
        for (java.lang.reflect.Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(item);
                Cell cell = row.createCell(cellNum++);
                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillClassesSheet(Sheet sheet) {
        List<Classe> classes = classeRepository.findAll();
        
        // Créer le style d'en-tête
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle dataStyle = createDataStyle(sheet.getWorkbook());
        
        // Créer l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Numéro", "Niveau", "Spécialité", "Nombre d'étudiants"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Remplir les données
        int rowNum = 1;
        for (Classe classe : classes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(classe.getId());
            row.createCell(1).setCellValue(classe.getNumber());
            
            if (classe.getNiveau() != null) {
                row.createCell(2).setCellValue(classe.getNiveau().getAnnee().toString());
                if (classe.getNiveau().getSpecialite() != null) {
                    row.createCell(3).setCellValue(classe.getNiveau().getSpecialite().getSpecialites().toString());
                }
            }
            
            // Appliquer le style aux cellules
            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Ajouter des filtres automatiques
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E" + (rowNum - 1)));
        
        // Auto-size les colonnes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillNiveauxSheet(Sheet sheet) {
        List<Niveau> niveaux = niveauRepository.findAll();
        
        // Créer le style d'en-tête
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle dataStyle = createDataStyle(sheet.getWorkbook());
        
        // Créer l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Année", "Spécialité", "Nombre de classes"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Remplir les données
        int rowNum = 1;
        for (Niveau niveau : niveaux) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(niveau.getId());
            row.createCell(1).setCellValue(niveau.getAnnee().toString());
            if (niveau.getSpecialite() != null) {
                row.createCell(2).setCellValue(niveau.getSpecialite().getSpecialites().toString());
            }
            row.createCell(3).setCellValue(niveau.getClasses() != null ? niveau.getClasses().size() : 0);
            
            // Appliquer le style aux cellules
            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Ajouter des filtres automatiques
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:D" + (rowNum - 1)));
        
        // Auto-size les colonnes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillSpecialitesSheet(Sheet sheet) {
        List<Specialite> specialites = specialiteRepository.findAll();
        
        // Créer le style d'en-tête
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle dataStyle = createDataStyle(sheet.getWorkbook());
        
        // Créer l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Spécialité", "Type de Formation", "Nombre de niveaux"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Remplir les données
        int rowNum = 1;
        for (Specialite specialite : specialites) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(specialite.getId());
            row.createCell(1).setCellValue(specialite.getSpecialites().toString());
            row.createCell(2).setCellValue(specialite.getTypeFormation().toString());
            row.createCell(3).setCellValue(specialite.getNiveaux() != null ? specialite.getNiveaux().size() : 0);
            
            // Appliquer le style aux cellules
            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Ajouter des filtres automatiques
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:D" + (rowNum - 1)));
        
        // Auto-size les colonnes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
} 