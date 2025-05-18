package esprit.git.gestionsummary_backgit_me.config;

import esprit.git.gestionsummary_backgit_me.Entities.Specialite;
import esprit.git.gestionsummary_backgit_me.Entities.Specialites;
import esprit.git.gestionsummary_backgit_me.Entities.TypeFormation;
import esprit.git.gestionsummary_backgit_me.Repositories.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class DataInitializer {

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @PostConstruct
    public void init() {
        if (specialiteRepository.count() == 0) {
            Specialite gl = new Specialite();
            gl.setNom("Génie Logiciel");
            gl.setSpecialites(Specialites.GL);
            gl.setTypeFormation(TypeFormation.ALTERNANT);
            gl.setNiveaux(new ArrayList<>());
            specialiteRepository.save(gl);

            Specialite ia = new Specialite();
            ia.setNom("Intelligence Artificielle");
            ia.setSpecialites(Specialites.IA);
            ia.setTypeFormation(TypeFormation.COURS_DU_JOUR);
            ia.setNiveaux(new ArrayList<>());
            specialiteRepository.save(ia);

            Specialite ds = new Specialite();
            ds.setNom("Data Science");
            ds.setSpecialites(Specialites.DS);
            ds.setTypeFormation(TypeFormation.COURS_DU_SOIR);
            ds.setNiveaux(new ArrayList<>());
            specialiteRepository.save(ds);

            Specialite iot = new Specialite();
            iot.setNom("Internet of Things");
            iot.setSpecialites(Specialites.IOT);
            iot.setTypeFormation(TypeFormation.ALTERNANT);
            iot.setNiveaux(new ArrayList<>());
            specialiteRepository.save(iot);
        }
    }
} 