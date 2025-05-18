import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApiService } from '../../services/api.service';
import { Classe } from '../../models/classe.model';
import { Eleve } from '../../models/eleve.model';
import { Repos } from '../../models/repos.model';
import { StatsDialogComponent } from './stats-dialog.component';

@Component({
  selector: 'app-classe-detail',
  templateUrl: './classe-detail.component.html',
  styleUrls: ['./classe-detail.component.scss']
})
export class ClasseDetailComponent implements OnInit {
  classe: Classe | null = null;
  eleves: Eleve[] = [];
  repos: Repos[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const classeId = this.route.snapshot.params['id'];
    this.loadClasse(classeId);
  }

  loadClasse(classeId: number): void {
    this.loading = true;
    this.apiService.getClasseById(classeId).subscribe({
      next: (data) => {
        this.classe = data;
        this.loadEleves(classeId);
        this.loadRepos(classeId);
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement de la classe';
        this.loading = false;
        this.showError(this.error);
      }
    });
  }

  loadEleves(classeId: number): void {
    this.apiService.getElevesByClasse(classeId).subscribe({
      next: (data) => {
        this.eleves = data;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des élèves';
        this.showError(this.error);
      }
    });
  }

  loadRepos(classeId: number): void {
    this.apiService.getReposByClasse(classeId).subscribe({
      next: (data) => {
        this.repos = data;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des repos';
        this.showError(this.error);
      }
    });
  }

  exportEleves(): void {
    if (this.classe) {
      this.apiService.exportEleves(this.classe.id!).subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `eleves_classe_${this.classe?.number}.xlsx`;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: (error) => {
          this.showError('Erreur lors de l\'export des élèves');
        }
      });
    }
  }

  exportRepos(): void {
    if (this.classe) {
      this.apiService.exportRepos(this.classe.id!).subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `repos_classe_${this.classe?.number}.xlsx`;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: (error) => {
          this.showError('Erreur lors de l\'export des repos');
        }
      });
    }
  }

  exportPresence(): void {
    if (this.classe) {
      this.apiService.exportPresence(this.classe.id!).subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `presence_classe_${this.classe?.number}.xlsx`;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: (error) => {
          this.showError('Erreur lors de l\'export de la présence');
        }
      });
    }
  }

  showStats(): void {
    if (this.classe) {
      this.apiService.getStatsClasse(this.classe.id!).subscribe({
        next: (stats) => {
          // Afficher les statistiques dans une boîte de dialogue
          this.dialog.open(StatsDialogComponent, {
            width: '600px',
            data: stats
          });
        },
        error: (error) => {
          this.showError('Erreur lors du chargement des statistiques');
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