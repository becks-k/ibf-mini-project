import { Component, OnInit, inject } from '@angular/core';
import { Mystery } from '../../models/game';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';
import { loadMysteries } from '../../states/mysteries/mystery.actions';

@Component({
  selector: 'app-mystery-detail',
  templateUrl: './mystery-detail.component.html',
  styleUrl: './mystery-detail.component.css'
})
export class MysteryDetailComponent implements OnInit {
  mystery$!: Observable<Mystery | undefined> 
  userId!: string
  mysteryId!: string

  private readonly store = inject(Store<AppState>)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly router = inject(Router)
  
  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      params => {
        this.mysteryId = params['mysteryId']
        this.userId = params['userId']
        this.store.dispatch(loadMysteries({ userId: this.userId }))
        this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))
      })
  }


  playMystery() {
    this.router.navigate(['/game', this.mysteryId])
  }

}
