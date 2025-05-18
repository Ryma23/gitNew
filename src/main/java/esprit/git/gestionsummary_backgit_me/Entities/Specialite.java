package esprit.git.gestionsummary_backgit_me.Entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @JsonProperty("specialites")
    private Specialites specialites;

    @Enumerated(EnumType.STRING)
    @JsonProperty("typeFormation")
    private TypeFormation typeFormation;

    @OneToMany(mappedBy = "specialite", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Niveau> niveaux = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Specialites getSpecialites() {
        return specialites;
    }

    public void setSpecialites(Specialites specialites) {
        this.specialites = specialites;
    }

    public TypeFormation getTypeFormation() {
        return typeFormation;
    }

    public void setTypeFormation(TypeFormation typeFormation) {
        this.typeFormation = typeFormation;
    }

    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(List<Niveau> niveaux) {
        this.niveaux = niveaux;
    }

    // Méthode utilitaire pour ajouter un niveau
    public void addNiveau(Niveau niveau) {
        niveaux.add(niveau);
        niveau.setSpecialite(this);
    }

    // Méthode utilitaire pour supprimer un niveau
    public void removeNiveau(Niveau niveau) {
        niveaux.remove(niveau);
        niveau.setSpecialite(null);
    }
}
