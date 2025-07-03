import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {RESummaryResponse} from '../models/resumen-material.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/';
  private nombreUsuarioSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('nombre'));
  public nombreUsuario$ = this.nombreUsuarioSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return new Observable(observer => {
      this.http.post(`${this.apiUrl}auth/login`, data, {
        withCredentials: true,
        observe: 'response'
      }).subscribe({
        next: res => {
          this.http.get<any>(`${this.apiUrl}auth/me`, { withCredentials: true }).subscribe(user => {
            sessionStorage.setItem('nombre', user.nombres);
            sessionStorage.setItem('email', user.email);
            sessionStorage.setItem('rol', user.rol);
            sessionStorage.setItem('id', user.id);
            this.nombreUsuarioSubject.next(user.nombres);
            observer.next(res);
          });
        },
        error: err => observer.error(err)
      });
    });
  }

  getNombreUsuario(): string | null {
    return this.nombreUsuarioSubject.value;
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}auth/register-user`, data);
  }

  createMaterial(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}api/v1/recursos-educativos/with-file`, data, {
      withCredentials: true
    });
  }

  filtrarMateriales(cursoId?: number): Observable<RESummaryResponse[]> {
    const params: any = {};
    if (cursoId) params.cursoId = cursoId;

    return this.http.get<RESummaryResponse[]>('http://localhost:8080/api/v1/recursos-educativos/filtrar', {
      withCredentials: true,
      params
    });
  }
}
