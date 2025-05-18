package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.*;
import esprit.git.gestionsummary_backgit_me.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImpl implements IService {

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private ReposRepository reposRepository;

    // Spécialités
    @Override
    public Page<Specialite> getAllSpecialites(Pageable pageable) {
        return specialiteRepository.findAll(pageable);
    }

    @Override
    public Optional<Specialite> getSpecialiteById(Long id) {
        return specialiteRepository.findById(id);
    }

    @Override
    public Specialite createSpecialite(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    @Override
    public Specialite updateSpecialite(Long id, Specialite specialite) {
        specialite.setId(id);
        return specialiteRepository.save(specialite);
    }

    @Override
    public void deleteSpecialite(Long id) {
        specialiteRepository.deleteById(id);
    }

    @Override
    public Page<Specialite> searchSpecialites(String search, Pageable pageable) {
        return specialiteRepository.findBySpecialitesContainingIgnoreCase(search, pageable);
    }

    // Niveaux
    @Override
    public Page<Niveau> getNiveauxBySpecialite(Long specialiteId, Pageable pageable) {
        return niveauRepository.findBySpecialiteId(specialiteId, pageable);
    }

    @Override
    public Optional<Niveau> getNiveauById(Long id) {
        return niveauRepository.findById(id);
    }

    @Override
    public Niveau saveNiveau(Niveau niveau) {
        return niveauRepository.save(niveau);
    }

    @Override
    public void deleteNiveau(Long id) {
        niveauRepository.deleteById(id);
    }

    @Override
    public Page<Niveau> searchNiveaux(Long specialiteId, String search, Pageable pageable) {
        return niveauRepository.findBySpecialiteIdAndAnneeContainingIgnoreCase(specialiteId, search, pageable);
    }

    // Classes
    @Override
    public Page<Classe> getClassesByNiveau(Long niveauId, Pageable pageable) {
        return classeRepository.findByNiveauId(niveauId, pageable);
    }

    @Override
    public Optional<Classe> getClasseById(Long id) {
        return classeRepository.findById(id);
    }

    @Override
    public Classe saveClasse(Classe classe) {
        return classeRepository.save(classe);
    }

    @Override
    public void deleteClasse(Long id) {
        classeRepository.deleteById(id);
    }

    @Override
    public Page<Classe> searchClasses(Long niveauId, String searchTerm, Pageable pageable) {
        return classeRepository.findByNiveauIdAndNumberContainingIgnoreCase(niveauId, searchTerm, pageable);
    }

    // Élèves
    @Override
    public List<Eleve> getElevesByClasse(Long classeId) {
        return eleveRepository.findByClasseId(classeId);
    }

    @Override
    public Eleve addEleve(Long classeId, Eleve eleve) {
        eleve.setClasse(classeRepository.findById(classeId).orElseThrow());
        return eleveRepository.save(eleve);
    }

    @Override
    public Eleve updateEleve(Eleve eleve) {
        return eleveRepository.save(eleve);
    }

    @Override
    public void deleteEleve(Long id) {
        eleveRepository.deleteById(id);
    }

    // Repos
    @Override
    public List<Repos> getReposByClasse(Long classeId) {
        return reposRepository.findByClasseId(classeId);
    }

    @Override
    public Repos addRepos(Long classeId, Repos repos) {
        repos.setClasse(classeRepository.findById(classeId).orElseThrow());
        return reposRepository.save(repos);
    }

    @Override
    public Repos updateRepos(Repos repos) {
        return reposRepository.save(repos);
    }

    @Override
    public void deleteRepos(Long id) {
        reposRepository.deleteById(id);
    }

    // Exports
    @Override
    public ResponseEntity<byte[]> exportEleves(Long classeId) {
        // Implémentation à faire
        return null;
    }

    @Override
    public ResponseEntity<byte[]> exportRepos(Long classeId) {
        // Implémentation à faire
        return null;
    }

    @Override
    public ResponseEntity<byte[]> exportPresence(Long classeId) {
        // Implémentation à faire
        return null;
    }

    // Statistiques
    @Override
    public DetailedStatistics getDetailedStatistics() {
        // Implémentation à faire
        return null;
    }

    @Override
    public DetailedStatistics getDetailedStatisticsByNiveau(Long niveauId) {
        // Implémentation à faire
        return null;
    }

    @Override
    public DetailedStatistics getDetailedStatisticsBySpecialite(Long specialiteId) {
        // Implémentation à faire
        return null;
    }
} 