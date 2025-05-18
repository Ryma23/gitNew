import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Specialite } from '../models/specialite.model';
import { Niveau } from '../models/niveau.model';
import { Classe } from '../models/classe.model';
import { Eleve } from '../models/eleve.model';
import { Repos } from '../models/repos.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = '/api';
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });

  constructor(private http: HttpClient) { }

  // Spécialités
  getSpecialites(): Observable<Specialite[]> {
    return this.http.get<Specialite[]>(`${this.baseUrl}/specialites`, { headers: this.headers });
  }

  addSpecialite(specialite: Specialite): Observable<Specialite> {
    return this.http.post<Specialite>(`${this.baseUrl}/specialites`, specialite, { headers: this.headers });
  }

  updateSpecialite(specialite: Specialite): Observable<Specialite> {
    return this.http.put<Specialite>(`${this.baseUrl}/specialites/${specialite.id}`, specialite, { headers: this.headers });
  }

  deleteSpecialite(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/specialites/${id}`, { headers: this.headers });
  }

  // Niveaux
  getNiveaux(): Observable<Niveau[]> {
    return this.http.get<Niveau[]>(`${this.baseUrl}/niveaux`, { headers: this.headers });
  }

  getNiveauxBySpecialite(specialiteId: number): Observable<Niveau[]> {
    return this.http.get<Niveau[]>(`${this.baseUrl}/specialites/${specialiteId}/niveaux`, { headers: this.headers });
  }

  addNiveau(niveau: Niveau): Observable<Niveau> {
    return this.http.post<Niveau>(`${this.baseUrl}/niveaux`, niveau, { headers: this.headers });
  }

  updateNiveau(niveau: Niveau): Observable<Niveau> {
    return this.http.put<Niveau>(`${this.baseUrl}/niveaux/${niveau.id}`, niveau, { headers: this.headers });
  }

  deleteNiveau(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/niveaux/${id}`, { headers: this.headers });
  }

  // Classes
  getClasses(): Observable<Classe[]> {
    return this.http.get<Classe[]>(`${this.baseUrl}/classes`, { headers: this.headers });
  }

  getClasseById(id: number): Observable<Classe> {
    return this.http.get<Classe>(`${this.baseUrl}/classes/${id}`, { headers: this.headers });
  }

  getClassesByNiveau(niveauId: number): Observable<Classe[]> {
    return this.http.get<Classe[]>(`${this.baseUrl}/niveaux/${niveauId}/classes`, { headers: this.headers });
  }

  addClasse(classe: Classe): Observable<Classe> {
    return this.http.post<Classe>(`${this.baseUrl}/classes`, classe, { headers: this.headers });
  }

  updateClasse(classe: Classe): Observable<Classe> {
    return this.http.put<Classe>(`${this.baseUrl}/classes/${classe.id}`, classe, { headers: this.headers });
  }

  deleteClasse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/classes/${id}`, { headers: this.headers });
  }

  // Élèves
  getElevesByClasse(classeId: number): Observable<Eleve[]> {
    return this.http.get<Eleve[]>(`${this.baseUrl}/classes/${classeId}/eleves`, { headers: this.headers });
  }

  addEleve(classeId: number, eleve: Eleve): Observable<Eleve> {
    return this.http.post<Eleve>(`${this.baseUrl}/classes/${classeId}/eleves`, eleve, { headers: this.headers });
  }

  updateEleve(eleve: Eleve): Observable<Eleve> {
    return this.http.put<Eleve>(`${this.baseUrl}/eleves/${eleve.id}`, eleve, { headers: this.headers });
  }

  deleteEleve(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/eleves/${id}`, { headers: this.headers });
  }

  // Repos
  getReposByClasse(classeId: number): Observable<Repos[]> {
    return this.http.get<Repos[]>(`${this.baseUrl}/classes/${classeId}/repos`, { headers: this.headers });
  }

  addRepos(classeId: number, repos: Repos): Observable<Repos> {
    return this.http.post<Repos>(`${this.baseUrl}/classes/${classeId}/repos`, repos, { headers: this.headers });
  }

  updateRepos(repos: Repos): Observable<Repos> {
    return this.http.put<Repos>(`${this.baseUrl}/repos/${repos.id}`, repos, { headers: this.headers });
  }

  deleteRepos(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/repos/${id}`, { headers: this.headers });
  }

  // Exports
  exportEleves(classeId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/classes/${classeId}/eleves/export`, { 
      headers: this.headers,
      responseType: 'blob'
    });
  }

  exportRepos(classeId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/classes/${classeId}/repos/export`, { 
      headers: this.headers,
      responseType: 'blob'
    });
  }

  exportPresence(classeId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/classes/${classeId}/presence/export`, { 
      headers: this.headers,
      responseType: 'blob'
    });
  }

  // Statistiques
  getStatsClasse(classeId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/classes/${classeId}/stats`, { headers: this.headers });
  }
}