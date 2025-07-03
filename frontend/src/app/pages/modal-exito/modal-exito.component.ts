import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-modal-exito',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  templateUrl: './modal-exito.component.html',
  styleUrls: ['./modal-exito.component.css']
})
export class ModalExitoComponent {
  constructor(public dialogRef: MatDialogRef<ModalExitoComponent>) {}

  cerrar(): void {
    this.dialogRef.close();
  }
}
