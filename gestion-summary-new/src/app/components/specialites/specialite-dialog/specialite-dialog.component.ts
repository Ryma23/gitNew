import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Specialite, Specialites, TypeFormation } from '../../../models/specialite.model';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-specialite-dialog',
  templateUrl: './specialite-dialog.component.html',
  styleUrls: ['./specialite-dialog.component.scss']
})
export class SpecialiteDialogComponent implements OnInit {
  specialiteForm: FormGroup;
  isEditMode: boolean;
  Specialites = Specialites;
  TypeFormation = TypeFormation;
  private headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Accept', 'application/json');

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<SpecialiteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Specialite
  ) {
    this.isEditMode = !!data;
    this.specialiteForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      specialites: ['', [Validators.required]],
      typeFormation: ['', [Validators.required]]
    });

    if (this.isEditMode) {
      this.specialiteForm.patchValue(data);
    }
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.specialiteForm.valid) {
      const formValue = this.specialiteForm.value;
      const specialite: Specialite = {
        id: this.isEditMode ? this.data.id : undefined,
        nom: formValue.nom.trim(),
        specialites: formValue.specialites as Specialites,
        typeFormation: formValue.typeFormation as TypeFormation,
        niveaux: this.isEditMode ? this.data.niveaux : []
      };

      // Validation supplémentaire
      if (!specialite.nom || !specialite.specialites || !specialite.typeFormation) {
        console.error('Données invalides:', specialite);
        return;
      }

      console.log('Sending specialite:', JSON.stringify(specialite, null, 2));
      this.dialogRef.close(specialite);
    } else {
      console.error('Form is invalid:', this.specialiteForm.errors);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 