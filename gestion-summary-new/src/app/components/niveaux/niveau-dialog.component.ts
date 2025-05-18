import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Niveau } from '../../models/niveau.model';
import { Specialite } from '../../models/specialite.model';

interface DialogData {
  niveau?: Niveau;
  specialite: Specialite;
}

@Component({
  selector: 'app-niveau-dialog',
  templateUrl: './niveau-dialog.component.html',
  styleUrls: ['./niveau-dialog.component.scss']
})
export class NiveauDialogComponent implements OnInit {
  form: FormGroup;
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<NiveauDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.isEditMode = !!data.niveau;
    this.form = this.fb.group({
      annee: ['', [Validators.required, Validators.pattern('^[0-9]{4}$')]]
    });

    if (this.isEditMode && data.niveau) {
      this.form.patchValue(data.niveau);
    }
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.form.valid) {
      const niveau: Niveau = {
        ...this.form.value,
        specialite: this.data.specialite
      };

      if (this.isEditMode && this.data.niveau) {
        niveau.id = this.data.niveau.id;
      }

      this.dialogRef.close(niveau);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 