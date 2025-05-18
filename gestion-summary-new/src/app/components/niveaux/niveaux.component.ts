import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from '../../services/api.service';
import { Niveau } from '../../models/niveau.model';
import { Specialite } from '../../models/specialite.model';
import { NiveauDialogComponent } from './niveau-dialog.component';

@Component({
  selector: 'app-niveaux',
  templateUrl: './niveaux.component.html',
  styleUrls: ['./niveaux.component.scss']
})
export class NiveauxComponent implements OnInit {
  niveaux: Niveau[] = [];
  specialites: Specialite[] = [];
  selectedSpecialite: Specialite | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private apiService: ApiService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadSpecialites();
  }

  loadSpecialites(): void {
    this.loading = true;
    this.apiService.getSpecialites().subscribe({
      next: (data) => {
        this.specialites = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des spécialités';
        this.loading = false;
        this.showError(this.error);
      }
    });
  }

  onSpecialiteSelect(specialite: Specialite): void {
    this.selectedSpecialite = specialite;
    this.loadNiveaux(specialite.id!);
  }

  loadNiveaux(specialiteId: number): void {
    this.loading = true;
    this.apiService.getNiveauxBySpecialite(specialiteId).subscribe({
      next: (data) => {
        this.niveaux = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des niveaux';
        this.loading = false;
        this.showError(this.error);
      }
    });
  }

  addNiveau(): void {
    if (!this.selectedSpecialite) {
      this.showError('Veuillez sélectionner une spécialité');
      return;
    }

    const dialogRef = this.dialog.open(NiveauDialogComponent, {
      width: '400px',
      data: { specialite: this.selectedSpecialite }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadNiveaux(this.selectedSpecialite!.id!);
        this.showSuccess('Niveau ajouté avec succès');
      }
    });
  }

  editNiveau(niveau: Niveau): void {
    const dialogRef = this.dialog.open(NiveauDialogComponent, {
      width: '400px',
      data: { niveau, specialite: this.selectedSpecialite }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadNiveaux(this.selectedSpecialite!.id!);
        this.showSuccess('Niveau modifié avec succès');
      }
    });
  }

  deleteNiveau(niveau: Niveau): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce niveau ?')) {
      this.apiService.deleteNiveau(niveau.id!).subscribe({
        next: () => {
          this.loadNiveaux(this.selectedSpecialite!.id!);
          this.showSuccess('Niveau supprimé avec succès');
        },
        error: (error) => {
          this.showError('Erreur lors de la suppression du niveau');
        }
      });
    }
  }

  showSuccess(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  showError(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
} 