import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Specialite } from '../../models/specialite.model';

@Component({
  selector: 'app-specialite-dialog',
  templateUrl: './specialite-dialog.component.html',
  styleUrls: ['./specialite-dialog.component.scss']
})
export class SpecialiteDialogComponent {
  form: FormGroup;
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<SpecialiteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { specialite?: Specialite }
  ) {
    this.isEditMode = !!data.specialite;
    this.form = this.fb.group({
      nom: [data.specialite?.nom || '', [Validators.required, Validators.minLength(3)]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const specialite: Specialite = {
        ...this.data.specialite,
        ...this.form.value
      };
      this.dialogRef.close(specialite);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 