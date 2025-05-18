package esprit.git.gestionsummary_backgit_me.Repositories;

import esprit.git.gestionsummary_backgit_me.Entities.Niveau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    Page<Niveau> findBySpecialiteId(Long specialiteId, Pageable pageable);
    Page<Niveau> findBySpecialiteIdAndAnneeContaining(Long specialiteId, String annee, Pageable pageable);
    List<Niveau> findBySpecialiteId(Long specialiteId);
    Page<Niveau> findBySpecialiteIdAndAnneeContainingIgnoreCase(Long specialiteId, String search, Pageable pageable);
} 