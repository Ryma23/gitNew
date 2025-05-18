package esprit.git.gestionsummary_backgit_me.Service;

import org.springframework.core.io.Resource;
import java.io.IOException;

public interface ExcelExportService {
    Resource exportClassesToExcel() throws IOException;
    Resource exportNiveauxToExcel() throws IOException;
    Resource exportSpecialitesToExcel() throws IOException;
    Resource exportCompleteSummaryToExcel() throws IOException;
} 