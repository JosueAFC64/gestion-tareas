import {Component} from '@angular/core';
import {RouterOutlet, RouterModule} from '@angular/router';
import {ModalAgregarMaterialComponent} from '../modal-agregar-material/modal-agregar-material.component';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-navbar-prin',
  imports: [
    RouterOutlet,
    MatDialogModule,
    MatButtonModule,
    RouterModule,
  ],
  templateUrl: './navbarPrin.html',
  styleUrl: './navbarPrin.css'
})

export class NavbarPrin {
  titulo = 'modal-material'
  constructor(private _matDialog: MatDialog) {
  }
  materialModal(): void{
    this._matDialog.open(ModalAgregarMaterialComponent,{
      width: '500px',
    })
  }
}
