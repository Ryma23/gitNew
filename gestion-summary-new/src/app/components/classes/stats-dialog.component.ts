import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

interface StatsData {
  totalEleves: number;
  totalAbsences: number;
  tauxAbsence: number;
  absencesParMois: { [key: string]: number };
}

@Component({
  selector: 'app-stats-dialog',
  templateUrl: './stats-dialog.component.html',
  styleUrls: ['./stats-dialog.component.scss']
})
export class StatsDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<StatsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: StatsData
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }

  getAbsencesParMoisArray(): { mois: string; nombre: number }[] {
    return Object.entries(this.data.absencesParMois).map(([mois, nombre]) => ({
      mois,
      nombre: Number(nombre)
    }));
  }
} 