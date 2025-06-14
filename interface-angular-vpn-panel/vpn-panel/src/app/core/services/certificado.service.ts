import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Certificado } from '../models/certificado.model';

@Injectable({
  providedIn: 'root',
})
export class CertificadoService {
  private apiUrl = '/api/certificados'; // Ajuste conforme o endpoint real

  constructor(private http: HttpClient) {}

  listar(filtro?: any): Observable<Certificado[]> {
    return this.http.get<Certificado[]>(this.apiUrl, { params: filtro });
  }

  criar(): Observable<any> {
    return this.http.post(this.apiUrl, {});
  }

  remover(ids: string[]): Observable<any> {
    return this.http.request('delete', this.apiUrl, { body: { ids } });
  }

  baixar(id: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/download`, { responseType: 'blob' });
  }
} 