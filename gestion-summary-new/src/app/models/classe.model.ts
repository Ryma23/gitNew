import { Niveau } from './niveau.model';
import { Eleve } from './eleve.model';
import { Repos } from './repos.model';

export interface Classe {
    id?: number;
    number: string;
    capacity: number;
    niveau?: Niveau;
    eleves?: Eleve[];
    repos?: Repos[];
} 