import {Component, OnInit} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from '../../services/auth.service';
import {RESummaryResponse} from '../../models/resumen-material.model';
import {CommonModule} from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class InicioComponent implements OnInit {
  filtroTitulo = '';
  filtroAutor = '';
  filtroFecha: string = '';
  ordenCampo: 'titulo' | 'fechaCreacion' = 'titulo';
  ordenDireccion: 'asc' | 'desc' = 'asc';
  materiales: RESummaryResponse[] = [];

  constructor(private auth: AuthService, private http: HttpClient) {
  }

  ngOnInit(): void {
    this.filtrar(); // carga todos
  }

  filtrar(cursoId?: number): void {
    this.auth.filtrarMateriales(cursoId).subscribe({
      next: res => this.materiales = res,
      error: err => console.error(err)
    });
  }

  setActive(categoria: string): void {
    const btnContenedor = document.getElementById('filtro-categoria');
    const btns = btnContenedor?.getElementsByClassName('filtro-btn');
    if (!btns) return;

    for (let i = 0; i < btns.length; i++) {
      btns[i].classList.remove('active');
      if (btns[i].id === categoria) btns[i].classList.add('active');
    }

    const categoriaMap: Record<string, number | undefined> = {
      'todos': undefined,
      'matematicas': 1,
      'ciencias': 2,
      'historia': 3,
      'idiomas': 4,
      'tecnologia': 5
    };

    this.filtrar(categoriaMap[categoria]);
  }

  aplicarFiltros(): void {
    this.auth.filtrarMateriales().subscribe({
      next: res => {
        let filtrado = res;

        // Filtro por título
        if (this.filtroTitulo.trim()) {
          filtrado = filtrado.filter(m =>
            m.titulo.toLowerCase().includes(this.filtroTitulo.trim().toLowerCase())
          );
        }

        // Filtro por autor
        if (this.filtroAutor.trim()) {
          filtrado = filtrado.filter(m =>
            String(m.creador ?? '').toLowerCase().includes(this.filtroAutor.trim().toLowerCase())
          );
        }

        // Filtro por fecha (solo fecha sin hora)
        if (this.filtroFecha) {
          const fechaBuscada = new Date(this.filtroFecha).toISOString().split('T')[0];
          filtrado = filtrado.filter(m =>
            m.fechaCreacion?.split('T')[0] === fechaBuscada
          );
        }

        // Ordenamiento
        filtrado.sort((a, b) => {
          const campoA = a[this.ordenCampo]?.toLowerCase?.() || a[this.ordenCampo];
          const campoB = b[this.ordenCampo]?.toLowerCase?.() || b[this.ordenCampo];

          if (this.ordenDireccion === 'asc') {
            return campoA > campoB ? 1 : campoA < campoB ? -1 : 0;
          } else {
            return campoA < campoB ? 1 : campoA > campoB ? -1 : 0;
          }
        });

        this.materiales = filtrado;
      },
      error: err => console.error(err)
    });
  }


  filtrarRapido(): void {
    // Solo aplica filtros por título y autor en tiempo real
    this.auth.filtrarMateriales().subscribe({
      next: res => {
        let filtrado = res;

        if (this.filtroTitulo.trim()) {
          filtrado = filtrado.filter(m =>
            m.titulo.toLowerCase().includes(this.filtroTitulo.trim().toLowerCase())
          );
        }

        if (this.filtroAutor.trim()) {
          filtrado = filtrado.filter(m =>
            String(m.creador ?? '').toLowerCase().includes(this.filtroAutor.trim().toLowerCase())
          );
        }

        this.materiales = filtrado;
      },
      error: err => console.error(err)
    });
  }

  eliminarMaterial(id: number): void {
    if (!confirm('¿Estás seguro de eliminar este material?')) return;

    const url = `http://localhost:8080/api/v1/recursos-educativos/${id}`;

    this.http.delete(url, { withCredentials: true }).subscribe({
      next: () => {
        this.materiales = this.materiales.filter(m => m.id !== id);
        alert('Material eliminado correctamente');
      },
      error: (err) => {
        console.error('Error al eliminar:', err);
        alert('Error al eliminar el material: ' + err.message);
      }
    });
  }

}
