import {Component} from '@angular/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AuthService} from '../../services/auth.service';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card'
import {MatDialog} from '@angular/material/dialog';
import {ModalExitoComponent} from '../modal-exito/modal-exito.component';

@Component({
  selector: 'app-modal-agregar-material',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule
  ],
  templateUrl: './modal-agregar-material.component.html',
  styleUrl: './modal-agregar-material.component.css'
})
export class ModalAgregarMaterialComponent {
  materialForm: FormGroup;
  selectedFile: File | null = null;
  isLoading = false;

  constructor(private fb: FormBuilder,
              private auth: AuthService,
              private dialog: MatDialog,
              public _matDialogRef: MatDialogRef<ModalAgregarMaterialComponent>) {
    this.materialForm = this.fb.group({
      titulo: ['', Validators.required],
      cursoId: ['', Validators.required],
      descripcion: ['', Validators.required],
      tipo: [''],
      creadorId: [''],
      file: [''],
      url: ['']
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];

      const fileType = this.selectedFile.type;
      console.log('Tipo MIME:', fileType);

      if (fileType.startsWith('application/pdf')) {
        this.materialForm.get('tipo')?.setValue('PDF');
      } else if (fileType.startsWith('video/')) {
        this.materialForm.get('tipo')?.setValue('VIDEO');
      } else if (fileType.startsWith('image/')) {
        this.materialForm.get('tipo')?.setValue('IMAGEN');
      } else {
        this.materialForm.get('tipo')?.setValue('OTRO');
      }
    }

  }

  onSubmit(): void {
    if (this.materialForm.invalid || this.isLoading) return;

    this.isLoading = true;

    const requestData = {
      titulo: this.materialForm.get('titulo')?.value,
      descripcion: this.materialForm.get('descripcion')?.value,
      tipo: this.materialForm.get('tipo')?.value,
      url: '',
      cursoId: parseInt(this.materialForm.get('cursoId')?.value)
    };

    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(requestData)], {type: 'application/json'}));
    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    this.auth.createMaterial(formData).subscribe({
      next: () => {
        this.isLoading = false;
        this.dialog.open(ModalExitoComponent);
        this._matDialogRef.close();
      },
      error: err => {
        console.error('Error al crear recurso', err);
        this.isLoading = false;
      }
    });
  }



  protected readonly event = event;

  cerrarModal(): void {
    this._matDialogRef.close();
  }


}
