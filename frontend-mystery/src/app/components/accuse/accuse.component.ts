import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GameService } from '../../services/game.service';
import { Observable } from 'rxjs';
import { Accuse, Mystery } from '../../models/game';
import { AppState } from '../../states/app.states';
import { Store } from '@ngrx/store';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';

@Component({
  selector: 'app-accuse',
  templateUrl: './accuse.component.html',
  styleUrl: './accuse.component.css'
})
export class AccuseComponent implements OnInit{
  accuseForm!: FormGroup
  mysteryId!: string
  mystery$!: Observable<Mystery | undefined>
  accusation: boolean = false
  userId: string = ''

  private readonly fb = inject(FormBuilder)
  private readonly router = inject(Router)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly store = inject(Store<AppState>)
  private gameService = inject(GameService)

  ngOnInit(): void {
    this.activatedRoute.parent?.params.subscribe(params => {
      this.mysteryId = params['mysteryId']
    })
    this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))
    this.createAccuseForm()
  }

  createAccuseForm() {
    this.accuseForm = this.fb.group({
      guilty: this.fb.control<string>('', [Validators.required]),
      weaponUsedInCrime: this.fb.control<string>('', [Validators.required]),
      guiltyMotive: this.fb.control<string>('', [Validators.required])
    })
  }

  submitAccuseForm() {
    this.accusation = true
    const user = sessionStorage.getItem('user')
    if (user) {
      this.userId = JSON.parse(user).id
    }
    const accuse: Accuse = {
      mysteryId: this.mysteryId,
      userId: this.userId,
      ...this.accuseForm.value
    }
    console.info('Accuse:', accuse)

    this.gameService.accuse(accuse)
      .then(outcome => {
        // console.info('Outcome:', outcome)
        this.accusation = false
        this.gameService.outcome = outcome
        // test
        // sessionStorage.setItem('outcome', JSON.stringify(outcome))
        this.router.navigate([`/game/${this.mysteryId}/outcome`])
        

      })
      .catch(error => {
        console.error('Error:', error)
        alert('Please input a valid OpenAI API key')
      })
  }
}
