import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/';

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}auth/login`, data, {
      withCredentials: true
    });
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}auth/register-user`, data);
  }

  createMaterial(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}api/v1/recursos-educativos/with-file`, data, {
      withCredentials: true
    });
  }

  test(): Observable<any> {
    return this.http.get(`${this.apiUrl}/test`, {
      withCredentials: true
    });
  }
}
