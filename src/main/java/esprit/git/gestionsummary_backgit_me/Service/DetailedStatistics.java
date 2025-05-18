package esprit.git.gestionsummary_backgit_me.Service;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class DetailedStatistics {
    private int totalClasses;
    private int totalNiveaux;
    private int totalSpecialites;
    private Map<String, Integer> classesByNiveau;
    private Map<String, Integer> classesBySpecialite;
    private Map<String, Integer> niveauxBySpecialite;
    private Map<String, Integer> specialitesByTypeFormation;
    private List<CapacityDistribution> capacityDistribution;
    private List<SpecialiteDistribution> specialiteDistribution;

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getTotalNiveaux() {
        return totalNiveaux;
    }

    public void setTotalNiveaux(int totalNiveaux) {
        this.totalNiveaux = totalNiveaux;
    }

    public int getTotalSpecialites() {
        return totalSpecialites;
    }

    public void setTotalSpecialites(int totalSpecialites) {
        this.totalSpecialites = totalSpecialites;
    }

    public Map<String, Integer> getClassesByNiveau() {
        return classesByNiveau;
    }

    public void setClassesByNiveau(Map<String, Integer> classesByNiveau) {
        this.classesByNiveau = classesByNiveau;
    }

    public Map<String, Integer> getClassesBySpecialite() {
        return classesBySpecialite;
    }

    public void setClassesBySpecialite(Map<String, Integer> classesBySpecialite) {
        this.classesBySpecialite = classesBySpecialite;
    }

    public Map<String, Integer> getNiveauxBySpecialite() {
        return niveauxBySpecialite;
    }

    public void setNiveauxBySpecialite(Map<String, Integer> niveauxBySpecialite) {
        this.niveauxBySpecialite = niveauxBySpecialite;
    }

    public Map<String, Integer> getSpecialitesByTypeFormation() {
        return specialitesByTypeFormation;
    }

    public void setSpecialitesByTypeFormation(Map<String, Integer> specialitesByTypeFormation) {
        this.specialitesByTypeFormation = specialitesByTypeFormation;
    }

    public List<CapacityDistribution> getCapacityDistribution() {
        return capacityDistribution;
    }

    public void setCapacityDistribution(List<CapacityDistribution> capacityDistribution) {
        this.capacityDistribution = capacityDistribution;
    }

    public List<SpecialiteDistribution> getSpecialiteDistribution() {
        return specialiteDistribution;
    }

    public void setSpecialiteDistribution(List<SpecialiteDistribution> specialiteDistribution) {
        this.specialiteDistribution = specialiteDistribution;
    }

    @Data
    public static class CapacityDistribution {
        private String range;
        private int count;

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    @Data
    public static class SpecialiteDistribution {
        private String specialite;
        private int totalClasses;
        private int totalNiveaux;
        private double averageClassesPerNiveau;

        public String getSpecialite() {
            return specialite;
        }

        public void setSpecialite(String specialite) {
            this.specialite = specialite;
        }

        public int getTotalClasses() {
            return totalClasses;
        }

        public void setTotalClasses(int totalClasses) {
            this.totalClasses = totalClasses;
        }

        public int getTotalNiveaux() {
            return totalNiveaux;
        }

        public void setTotalNiveaux(int totalNiveaux) {
            this.totalNiveaux = totalNiveaux;
        }

        public double getAverageClassesPerNiveau() {
            return averageClassesPerNiveau;
        }

        public void setAverageClassesPerNiveau(double averageClassesPerNiveau) {
            this.averageClassesPerNiveau = averageClassesPerNiveau;
        }
    }
} 