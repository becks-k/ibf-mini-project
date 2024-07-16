import { Component, Input, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { loadMysteries } from '../../states/mysteries/mystery.actions';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  userId!: string

  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly router = inject(Router)
  private store = inject(Store<AppState>)

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      params => {
        this.userId = params['userId']
        this.store.dispatch(loadMysteries({ userId: this.userId }))

      }
    )
  }


}
