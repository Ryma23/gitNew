import { Classe } from './classe.model';
import { Repos } from './repos.model';

export interface Eleve {
    id?: number;
    nom: string;
    prenom: string;
    email: string;
    numeroEtudiant: string;
    classe?: Classe;
    repos?: Repos[];
} 