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
    // Crear el objeto RERequest como JSON
    const requestData = {
      titulo: this.materialForm.get('titulo')?.value,
      descripcion: this.materialForm.get('descripcion')?.value,
      tipo: this.materialForm.get('tipo')?.value,
      url: '',
      creadorId: 3,
      cursoId: parseInt(this.materialForm.get('cursoId')?.value)
    };

    const formData = new FormData();
    
    // Agregar el objeto request como JSON string
    formData.append('request', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));
    
    // Agregar el archivo si existe
    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    console.log('Request data:', requestData);
    console.log('File:', this.selectedFile);

    this.auth.createMaterial(formData)
      .subscribe({
        next: res => {
          console.log('Recurso creado', res);
          this._matDialogRef.close();
        },
        error: err => console.error('Error al crear recurso', err)
      });
  }

  protected readonly event = event;
}
