import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from '../models/login';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  baseUrl: string = "/api"

  private readonly http = inject(HttpClient)

  login(username: string, password: string): Promise<User> {
    return firstValueFrom(this.http.post<User>(`${this.baseUrl}/login`, {username, password}))
  }

  register(email: string, username: string, password: string): Promise<User> {
    return firstValueFrom(this.http.post<User>(`${this.baseUrl}/register`, {email, username, password}))
  }

  checkIfUserExists(username: string): Promise<any> {
    return firstValueFrom(this.http.post<any>(`${this.baseUrl}/forgot-password`, username))
  }
  
  checkPasswordResetToken(token: string): Promise<any> {
    const params = new HttpParams()
      .set('token', token)
    return firstValueFrom(this.http.get<any>(`${this.baseUrl}/reset-password`, {params}))
  }

  resetPassword(token: string, password: string): Promise<any> {
      return firstValueFrom(this.http.post<any>(`${this.baseUrl}/reset-password`, {token, password}))
  }


  isAuthenticated(): boolean {
    return this.getAuthToken() !== null;
  }

  getAuthToken(): string | null {
    return window.sessionStorage.getItem("auth_token");
  }

  setAuthToken(token: string | null) {
    if (token != null) {
      window.sessionStorage.setItem("auth_token", token)
    } else {
      window.sessionStorage.removeItem("auth_token")
    }
  }


}
