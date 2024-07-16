import { Component, OnInit, inject } from '@angular/core';
import { Mystery } from '../../models/game';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { loadMysteries } from '../../states/mysteries/mystery.actions';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent implements OnInit {
  mysteryId!: string
  mystery$!: Observable<Mystery | undefined>
  

  private readonly activatedRoute = inject(ActivatedRoute)
  private store = inject(Store<AppState>)

  ngOnInit(): void {
    this.mysteryId = this.activatedRoute.snapshot.params['mysteryId']
    const user = sessionStorage.getItem('user')
    if (user) {
      const userId = JSON.parse(user).id
      this.store.dispatch(loadMysteries({ userId: userId }))
      this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))      
    }
  }
}
