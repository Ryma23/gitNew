package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.Repos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReposService {
    Repos ajoutRepos(Repos repos);
    Repos modifierRepos(Repos repos);
    void deleteRepos(Long id);
    List<Repos> lireRepos();
    List<Repos> lireReposByClasse(Long classeId);
    List<Repos> lireReposByEleve(Long eleveId);
    Page<Repos> getReposByClasse(Long classeId, Pageable pageable);
    Optional<Repos> getReposById(Long id);
    List<Repos> getReposByDateRange(Long classeId, LocalDateTime startDate, LocalDateTime endDate);
    List<Repos> getReposByStatut(Long classeId, String statut);
    void updateReposStatut(Long reposId, String statut);
    byte[] exportReposToExcel(Long classeId) throws IOException;
} 