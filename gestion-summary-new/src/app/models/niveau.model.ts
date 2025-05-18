import { Specialite } from './specialite.model';
import { Classe } from './classe.model';

export interface Niveau {
    id?: number;
    annee: string;
    specialite?: Specialite;
    classes?: Classe[];
} 