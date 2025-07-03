import { Component, OnInit } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { ModalAgregarMaterialComponent } from '../modal-agregar-material/modal-agregar-material.component';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar-prin',
  standalone: true,
  imports: [
    RouterOutlet,
    MatDialogModule,
    MatButtonModule,
    RouterModule,
  ],
  templateUrl: './navbarPrin.html',
  styleUrl: './navbarPrin.css'
})
export class NavbarPrin implements OnInit {
  nombreUsuario: string | null = null;
  titulo = 'modal-material';

  constructor(private _matDialog: MatDialog, private auth: AuthService) {}

  ngOnInit(): void {
    this.nombreUsuario = this.auth.getNombreUsuario();
  }

  materialModal(): void {
    this._matDialog.open(ModalAgregarMaterialComponent, {
      width: '500px',
    });
  }

  logout(): void {
    fetch('http://localhost:8080/auth/logout', {
      method: 'POST',
      credentials: 'include'
    })
      .then(() => {
        localStorage.clear();
        window.location.href = '/login';
      })
      .catch(err => console.error('Error al cerrar sesión', err));
  }

}
