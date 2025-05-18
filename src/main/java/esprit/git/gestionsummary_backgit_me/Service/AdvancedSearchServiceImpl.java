package esprit.git.gestionsummary_backgit_me.Service;

import esprit.git.gestionsummary_backgit_me.Entities.*;
import esprit.git.gestionsummary_backgit_me.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancedSearchServiceImpl implements AdvancedSearchService {

    @Autowired
    private SpecialiteRepository specialiteRepository;
    
    @Autowired
    private NiveauRepository niveauRepository;
    
    @Autowired
    private ClasseRepository classeRepository;

    // Méthodes de recherche avec pagination
    @Override
    public Page<Classe> searchClassesByNiveau(Long niveauId, PaginationRequest pagination) {
        List<Classe> classes = searchClassesByNiveau(niveauId);
        return createPage(classes, pagination);
    }

    @Override
    public Page<Classe> searchClassesBySpecialite(Long specialiteId, PaginationRequest pagination) {
        List<Classe> classes = searchClassesBySpecialite(specialiteId);
        return createPage(classes, pagination);
    }

    @Override
    public Page<Classe> searchClassesByCapacity(int minCapacity, int maxCapacity, PaginationRequest pagination) {
        List<Classe> classes = searchClassesByCapacity(minCapacity, maxCapacity);
        return createPage(classes, pagination);
    }

    @Override
    public Page<Classe> searchClassesMultiCriteria(Long niveauId, Long specialiteId, Integer minCapacity, Integer maxCapacity, PaginationRequest pagination) {
        List<Classe> classes = searchClassesMultiCriteria(niveauId, specialiteId, minCapacity, maxCapacity);
        return createPage(classes, pagination);
    }

    @Override
    public Page<Niveau> searchNiveauxBySpecialite(Long specialiteId, PaginationRequest pagination) {
        List<Niveau> niveaux = searchNiveauxBySpecialite(specialiteId);
        return createPage(niveaux, pagination);
    }

    @Override
    public Page<Niveau> searchNiveauxByAnnee(String annee, PaginationRequest pagination) {
        List<Niveau> niveaux = searchNiveauxByAnnee(annee);
        return createPage(niveaux, pagination);
    }

    @Override
    public Page<Specialite> searchSpecialitesByTypeFormation(String typeFormation, PaginationRequest pagination) {
        List<Specialite> specialites = searchSpecialitesByTypeFormation(typeFormation);
        return createPage(specialites, pagination);
    }

    @Override
    public Page<Specialite> searchSpecialitesByNiveau(Long niveauId, PaginationRequest pagination) {
        List<Specialite> specialites = searchSpecialitesByNiveau(niveauId);
        return createPage(specialites, pagination);
    }

    // Méthodes de recherche sans pagination
    @Override
    public List<Classe> searchClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId);
    }

    @Override
    public List<Classe> searchClassesBySpecialite(Long specialiteId) {
        return classeRepository.findAll().stream()
                .filter(classe -> classe.getNiveau() != null && 
                        classe.getNiveau().getSpecialite() != null && 
                        classe.getNiveau().getSpecialite().getId().equals(specialiteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Classe> searchClassesByCapacity(int minCapacity, int maxCapacity) {
        return classeRepository.findAll().stream()
                .filter(classe -> classe.getCapacity() >= minCapacity && classe.getCapacity() <= maxCapacity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Classe> searchClassesMultiCriteria(Long niveauId, Long specialiteId, Integer minCapacity, Integer maxCapacity) {
        return classeRepository.findAll().stream()
                .filter(classe -> {
                    boolean matchesNiveau = niveauId == null || 
                            (classe.getNiveau() != null && classe.getNiveau().getId().equals(niveauId));
                    
                    boolean matchesSpecialite = specialiteId == null || 
                            (classe.getNiveau() != null && classe.getNiveau().getSpecialite() != null && 
                            classe.getNiveau().getSpecialite().getId().equals(specialiteId));
                    
                    boolean matchesCapacity = (minCapacity == null || classe.getCapacity() >= minCapacity) && 
                            (maxCapacity == null || classe.getCapacity() <= maxCapacity);
                    
                    return matchesNiveau && matchesSpecialite && matchesCapacity;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Niveau> searchNiveauxBySpecialite(Long specialiteId) {
        return niveauRepository.findBySpecialiteId(specialiteId);
    }

    @Override
    public List<Niveau> searchNiveauxByAnnee(String annee) {
        return niveauRepository.findAll().stream()
                .filter(niveau -> niveau.getAnnee().toString().equals(annee))
                .collect(Collectors.toList());
    }

    @Override
    public List<Specialite> searchSpecialitesByTypeFormation(String typeFormation) {
        return specialiteRepository.findAll().stream()
                .filter(specialite -> specialite.getTypeFormation().toString().equals(typeFormation))
                .collect(Collectors.toList());
    }

    @Override
    public List<Specialite> searchSpecialitesByNiveau(Long niveauId) {
        return niveauRepository.findById(niveauId)
                .map(niveau -> {
                    List<Specialite> result = new ArrayList<>();
                    if (niveau.getSpecialite() != null) {
                        result.add(niveau.getSpecialite());
                    }
                    return result;
                })
                .orElse(new ArrayList<>());
    }

    // Méthodes de statistiques
    @Override
    public int getTotalClassesByNiveau(Long niveauId) {
        return (int) classeRepository.findByNiveauId(niveauId).size();
    }

    @Override
    public int getTotalClassesBySpecialite(Long specialiteId) {
        return (int) searchClassesBySpecialite(specialiteId).size();
    }

    @Override
    public int getTotalNiveauxBySpecialite(Long specialiteId) {
        return (int) niveauRepository.findBySpecialiteId(specialiteId).size();
    }

    @Override
    public int getTotalSpecialitesByTypeFormation(String typeFormation) {
        return (int) searchSpecialitesByTypeFormation(typeFormation).size();
    }

    @Override
    public DetailedStatistics getDetailedStatistics() {
        DetailedStatistics stats = new DetailedStatistics();
        
        // Totaux
        stats.setTotalClasses((int) classeRepository.count());
        stats.setTotalNiveaux((int) niveauRepository.count());
        stats.setTotalSpecialites((int) specialiteRepository.count());

        // Distribution par niveau
        Map<String, Integer> classesByNiveau = new HashMap<>();
        classeRepository.findAll().forEach(classe -> {
            if (classe.getNiveau() != null) {
                String niveau = classe.getNiveau().getAnnee().toString();
                classesByNiveau.merge(niveau, 1, Integer::sum);
            }
        });
        stats.setClassesByNiveau(classesByNiveau);

        // Distribution par spécialité
        Map<String, Integer> classesBySpecialite = new HashMap<>();
        classeRepository.findAll().forEach(classe -> {
            if (classe.getNiveau() != null && classe.getNiveau().getSpecialite() != null) {
                String specialite = classe.getNiveau().getSpecialite().getSpecialites().toString();
                classesBySpecialite.merge(specialite, 1, Integer::sum);
            }
        });
        stats.setClassesBySpecialite(classesBySpecialite);

        return stats;
    }

    @Override
    public DetailedStatistics getDetailedStatisticsByNiveau(Long niveauId) {
        DetailedStatistics stats = new DetailedStatistics();
        Niveau niveau = niveauRepository.findById(niveauId).orElse(null);
        
        if (niveau != null) {
            stats.setTotalClasses(getTotalClassesByNiveau(niveauId));
            stats.setTotalNiveaux(1);
            stats.setTotalSpecialites(niveau.getSpecialite() != null ? 1 : 0);

            // Distribution des classes pour ce niveau
            Map<String, Integer> classesByNiveau = new HashMap<>();
            classesByNiveau.put(niveau.getAnnee().toString(), getTotalClassesByNiveau(niveauId));
            stats.setClassesByNiveau(classesByNiveau);

            // Distribution des spécialités pour ce niveau
            if (niveau.getSpecialite() != null) {
                Map<String, Integer> specialitesByType = new HashMap<>();
                specialitesByType.put(niveau.getSpecialite().getTypeFormation().toString(), 1);
                stats.setSpecialitesByTypeFormation(specialitesByType);
            }
        }

        return stats;
    }

    @Override
    public DetailedStatistics getDetailedStatisticsBySpecialite(Long specialiteId) {
        DetailedStatistics stats = new DetailedStatistics();
        Specialite specialite = specialiteRepository.findById(specialiteId).orElse(null);
        
        if (specialite != null) {
            stats.setTotalClasses(getTotalClassesBySpecialite(specialiteId));
            stats.setTotalNiveaux(getTotalNiveauxBySpecialite(specialiteId));
            stats.setTotalSpecialites(1);

            // Distribution des classes par niveau pour cette spécialité
            Map<String, Integer> classesByNiveau = new HashMap<>();
            niveauRepository.findBySpecialiteId(specialiteId).forEach(niveau -> {
                String niveauStr = niveau.getAnnee().toString();
                int count = getTotalClassesByNiveau(niveau.getId());
                classesByNiveau.put(niveauStr, count);
            });
            stats.setClassesByNiveau(classesByNiveau);

            // Distribution des spécialités par type
            Map<String, Integer> specialitesByType = new HashMap<>();
            specialitesByType.put(specialite.getTypeFormation().toString(), 1);
            stats.setSpecialitesByTypeFormation(specialitesByType);
        }

        return stats;
    }

    // Méthodes de recherche avec Pageable
    @Override
    public Page<Specialite> searchSpecialites(String searchTerm, Pageable pageable) {
        return specialiteRepository.findBySpecialitesContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public Page<Niveau> searchNiveaux(Long specialiteId, String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return niveauRepository.findBySpecialiteIdAndAnneeContaining(specialiteId, searchTerm, pageable);
        }
        return niveauRepository.findBySpecialiteId(specialiteId, pageable);
    }

    @Override
    public Page<Classe> searchClasses(Long niveauId, String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return classeRepository.findByNiveauIdAndNumberContainingIgnoreCase(niveauId, searchTerm, pageable);
        }
        return classeRepository.findByNiveauId(niveauId, pageable);
    }

    @Override
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }

    @Override
    public List<Niveau> getNiveauxBySpecialite(Long specialiteId) {
        return niveauRepository.findBySpecialiteId(specialiteId);
    }

    @Override
    public List<Classe> getClassesByNiveau(Long niveauId) {
        return classeRepository.findByNiveauId(niveauId);
    }

    @Override
    public Optional<Specialite> getSpecialiteById(Long id) {
        return specialiteRepository.findById(id);
    }

    @Override
    public Optional<Niveau> getNiveauById(Long id) {
        return niveauRepository.findById(id);
    }

    @Override
    public Optional<Classe> getClasseById(Long id) {
        return classeRepository.findById(id);
    }

    // Méthode utilitaire pour la pagination
    private <T> Page<T> createPage(List<T> items, PaginationRequest pagination) {
        int start = (int) PageRequest.of(pagination.getPage(), pagination.getSize()).getOffset();
        int end = Math.min((start + pagination.getSize()), items.size());

        if (start >= items.size()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(pagination.getPage(), pagination.getSize()), items.size());
        }

        List<T> pageContent = items.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(pagination.getPage(), pagination.getSize()), items.size());
    }
} 