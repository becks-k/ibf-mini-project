import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GameService } from '../../services/game.service';
import { Mystery, Observation } from '../../models/game';
import { Observable } from 'rxjs';
import { AppState } from '../../states/app.states';
import { Store } from '@ngrx/store';
import { selectMysteryById } from '../../states/mysteries/mystery.selectors';


@Component({
  selector: 'app-crime-scene',
  templateUrl: './crime-scene.component.html',
  styleUrl: './crime-scene.component.css'
})
export class CrimeSceneComponent implements OnInit {
  crimeForm!: FormGroup
  mysteryId!: string
  observation!: Observation 
  mystery$!: Observable<Mystery | undefined>
  messageSent: boolean = false
  username: string = ''

  private readonly store = inject(Store<AppState>)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly fb = inject(FormBuilder)
  private readonly gameService = inject(GameService) 

  ngOnInit(): void {
    this.activatedRoute.parent?.params.subscribe(params => {
      this.mysteryId = params['mysteryId']
    })
    this.mystery$ = this.store.select(selectMysteryById(this.mysteryId))
    this.createCrimeForm()
  }

  createCrimeForm() {
    this.crimeForm = this.fb.group({
      text: this.fb.control<string>('', [Validators.required])
    })

  this.messageSent = false
  }

  sendMessage(text: string) {
    this.messageSent = true
    if (this.observation) {
      this.observation.text = ''
    }
    if (this.crimeForm.valid) {
      const user = sessionStorage.getItem('user')
      if (user) {
        this.username = JSON.parse(user).username
      }
      const observation: Observation = {
        sender: this.username,
        mysteryId: this.mysteryId,
        text: text
      }
      console.info('Action:', observation)
      this.gameService.searchScene(observation)
        .then(result => {
          this.observation = result
          this.messageSent = false
          this.crimeForm.reset()
        })
        .catch(error => {
          console.error('Error:', error)
          this.messageSent = false
          this.crimeForm.reset()
          alert('Please input a valid OpenAI API key')
        })
    }
  }
}
