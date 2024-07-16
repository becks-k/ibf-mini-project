import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Accuse, ApiKey, Message, Mystery, Observation, Outcome, PlayerStat, Response } from '../models/game';
import { Observable, lastValueFrom } from 'rxjs';
import { User } from '../models/login';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  baseUrl: string = "/api"
  outcome?: Outcome

  private readonly http = inject(HttpClient)

  getMysteries(userId: string): Observable<Mystery[]> {
    return this.http.get<Mystery[]>(`${this.baseUrl}/mysteries/${userId}`)
  }

  deleteMystery(mysteryId: string): Observable<Response> {
    return this.http.delete<Response>(`${this.baseUrl}/delete/${mysteryId}`)
  }

  generateMystery(theme: string, userId: string): Observable<Mystery> {
    const params = new HttpParams()
      .set('theme', theme)

    return this.http.get<Mystery>(`${this.baseUrl}/generate/${userId}`, {params})
  }

  chat(message: Message): Observable<Message> {
    return this.http.post<Message>(`${this.baseUrl}/chat`, message)
  }

  searchScene(observation: Observation): Promise<Observation> {
    return lastValueFrom(this.http.post<Observation>(`${this.baseUrl}/search`, observation))
  }

  accuse(accuse: Accuse): Promise<Outcome> {
    return lastValueFrom(this.http.post<Outcome>(`${this.baseUrl}/accuse`, accuse))
  }

  submitApiKey(apiKey: ApiKey): Promise<Response> {
    return lastValueFrom(this.http.post<Response>(`${this.baseUrl}/cache-api-key`, apiKey))
  }

  getLeaderboard(): Promise<PlayerStat[]> {
    return lastValueFrom(this.http.get<PlayerStat[]>(`${this.baseUrl}/leaderboard`))
  }

  removeApiKey():Promise<any> {
    return lastValueFrom(this.http.delete<any>(`${this.baseUrl}/remove-api-key`))
  }

  getApiKey(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/retrieve-api-key`)
  }

  
}
