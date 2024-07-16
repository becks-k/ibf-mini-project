import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Mystery } from '../../models/game';
import { ActivatedRoute } from '@angular/router';
import { AppState } from '../../states/app.states';
import { Store } from '@ngrx/store';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrl: './article.component.css'
})
export class ArticleComponent implements OnInit {
  mysteryId!: string
  mystery$!: Observable<Mystery | undefined>

  private readonly activatedRoute = inject(ActivatedRoute)
  private store = inject(Store<AppState>)
  
  ngOnInit(): void {
    this.mysteryId = this.activatedRoute.parent?.snapshot.params['mysteryId']
    this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))
  }
}
