import { Component } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card'

@Component({
  selector: 'app-modal-agregar-material',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './modal-agregar-material.component.html',
  styleUrl: './modal-agregar-material.component.css'
})
export class ModalAgregarMaterialComponent {
  materialForm: FormGroup;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder, private auth: AuthService, public _matDialogRef: MatDialogRef<ModalAgregarMaterialComponent>) {
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
    const formData = new FormData();
    formData.append('titulo', this.materialForm.get('titulo')?.value)
    formData.append('descripcion', this.materialForm.get('descripcion')?.value)
    formData.append('url', '')
    formData.append('cursoId', this.materialForm.get('cursoId')?.value)
    formData.append('creadorId', '3')
    formData.append('tipo', this.materialForm.get('tipo')?.value)

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    console.log(formData.get('titulo'))
    console.log(formData.get('descripcion'))
    console.log(formData.get('url'))
    console.log(formData.get('cursoId'))
    console.log(formData.get('creadorId'))
    console.log(formData.get('tipo'))
    console.log(formData.get('file'))

    this.auth.createMaterial(formData)
      .subscribe({
        next: res => console.log('Recurso creado', res),
        error: err => console.error('Error al crear recurso', err)
      });
  }

  protected readonly event = event;
}
