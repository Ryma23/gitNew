import { Classe } from './classe.model';
import { Eleve } from './eleve.model';

export interface Repos {
    id?: number;
    date: Date;
    motif: string;
    classe?: Classe;
    eleve?: Eleve;
} 