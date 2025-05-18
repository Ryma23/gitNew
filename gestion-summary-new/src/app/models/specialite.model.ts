export enum Specialites {
  GL = 'GL',
  IA = 'IA',
  DS = 'DS',
  IOT = 'IOT',
  TRANC_COMMUN = 'TRANC_COMMUN'
}

export enum TypeFormation {
  ALTERNANT = 'ALTERNANT',
  COURS_DU_JOUR = 'COURS_DU_JOUR',
  COURS_DU_SOIR = 'COURS_DU_SOIR'
}

export interface Specialite {
  id?: number;
  nom: string;
  specialites: Specialites;
  typeFormation: TypeFormation;
  niveaux?: any[];
} 