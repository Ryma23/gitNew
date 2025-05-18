package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Eleve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EleveService {
    Eleve ajoutEleve(Eleve eleve);
    Eleve modifierEleve(Eleve eleve);
    void deleteEleve(Long id);
    List<Eleve> lireEleves();
    List<Eleve> lireElevesByClasse(Long classeId);
    Page<Eleve> getElevesByClasse(Long classeId, Pageable pageable);
    Page<Eleve> searchEleves(Long classeId, String searchTerm, Pageable pageable);
    Optional<Eleve> getEleveById(Long id);
    void importElevesFromExcel(MultipartFile file, Long classeId) throws IOException;
    byte[] exportElevesToExcel(Long classeId) throws IOException;
    byte[] exportListePresence(Long classeId) throws IOException;
} 