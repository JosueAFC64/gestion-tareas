import { Component } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-modal-agregar-material',
  imports: [],
  templateUrl: './modal-agregar-material.component.html',
  styleUrl: './modal-agregar-material.component.css'
})
export class ModalAgregarMaterialComponent {
  constructor(public _matDialogRef: MatDialogRef<ModalAgregarMaterialComponent>) {
  }
}
