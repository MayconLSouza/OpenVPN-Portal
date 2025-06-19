import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, from, of } from 'rxjs';
import { Certificado } from '../models/certificado.model';
import { concatMap, catchError, toArray } from 'rxjs/operators';

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

  remover(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  removerVarios(ids: string[]): Observable<any[]> {
    return from(ids).pipe(
      concatMap(id =>
        this.remover(id).pipe(
          catchError(error => of({ id, error }))
        )
      ),
      toArray()
    );
  }

  baixar(id: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/download`, { responseType: 'blob' });
  }
} 