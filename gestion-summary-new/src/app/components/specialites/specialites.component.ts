import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ApiService } from '../../services/api.service';
import { Specialite } from '../../models/specialite.model';
import { Niveau } from '../../models/niveau.model';
import { Classe } from '../../models/classe.model';
import { SpecialiteDialogComponent } from './specialite-dialog.component';

@Component({
  selector: 'app-specialites',
  templateUrl: './specialites.component.html',
  styleUrls: ['./specialites.component.scss']
})
export class SpecialitesComponent implements OnInit {
  displayedColumns: string[] = ['id', 'nom', 'actions'];
  dataSource: MatTableDataSource<Specialite>;
  specialites: Specialite[] = [];
  niveaux: Niveau[] = [];
  classes: Classe[] = [];
  selectedSpecialite: Specialite | null = null;
  selectedNiveau: Niveau | null = null;
  loading = false;
  error: string | null = null;
  totalItems = 0;
  pageSize = 10;
  currentPage = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private apiService: ApiService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.dataSource = new MatTableDataSource<Specialite>([]);
  }

  ngOnInit(): void {
    this.loadSpecialites();
  }

  loadSpecialites(): void {
    this.loading = true;
    this.error = null;
    this.apiService.getSpecialites().subscribe({
      next: (data) => {
        this.specialites = data;
        this.dataSource.data = data;
        this.totalItems = data.length;
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

  onNiveauSelect(niveau: Niveau): void {
    this.selectedNiveau = niveau;
    this.loadClasses(niveau.id!);
  }

  loadClasses(niveauId: number): void {
    this.loading = true;
    this.apiService.getClassesByNiveau(niveauId).subscribe({
      next: (data) => {
        this.classes = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des classes';
        this.loading = false;
        this.showError(this.error);
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadSpecialites();
  }

  addSpecialite(): void {
    const dialogRef = this.dialog.open(SpecialiteDialogComponent, {
      width: '400px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.apiService.addSpecialite(result).subscribe({
          next: () => {
            this.loadSpecialites();
            this.showSuccess('Spécialité ajoutée avec succès');
          },
          error: (error) => {
            this.showError('Erreur lors de l\'ajout de la spécialité');
          }
        });
      }
    });
  }

  editSpecialite(specialite: Specialite): void {
    const dialogRef = this.dialog.open(SpecialiteDialogComponent, {
      width: '400px',
      data: { specialite }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.apiService.updateSpecialite(result).subscribe({
          next: () => {
            this.loadSpecialites();
            this.showSuccess('Spécialité modifiée avec succès');
          },
          error: (error) => {
            this.showError('Erreur lors de la modification de la spécialité');
          }
        });
      }
    });
  }

  deleteSpecialite(specialite: Specialite): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette spécialité ?')) {
      this.apiService.deleteSpecialite(specialite.id!).subscribe({
        next: () => {
          this.loadSpecialites();
          this.showSuccess('Spécialité supprimée avec succès');
        },
        error: (error) => {
          this.showError('Erreur lors de la suppression de la spécialité');
        }
      });
    }
  }

  exportToExcel(): void {
    // TODO: Implémenter l'export Excel
    this.snackBar.open('Fonctionnalité à implémenter', 'Fermer', { duration: 3000 });
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