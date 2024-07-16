import { Component, OnInit, inject } from '@angular/core';
import { Mystery } from '../../models/game';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  mysteryId!: string
  mystery$!: Observable<Mystery | undefined>

  private readonly activatedRoute = inject(ActivatedRoute)
  private store = inject(Store<AppState>)

  ngOnInit(): void {
    this.mysteryId = this.activatedRoute.snapshot.params['mysteryId']
    this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))
  }

}
