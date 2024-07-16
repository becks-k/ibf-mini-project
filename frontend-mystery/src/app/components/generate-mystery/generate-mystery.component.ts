import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GameService } from '../../services/game.service';
import { Mystery } from '../../models/game';
import { select, Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { addMystery, checkApiKey } from '../../states/mysteries/mystery.actions';
import { selectAddMysterySuccess, selectApiKeyPresent, selectMysteryError } from '../../states/mysteries/mystery.selectors';
import { take } from 'rxjs';

@Component({
  selector: 'app-generate-mystery',
  templateUrl: './generate-mystery.component.html',
  styleUrl: './generate-mystery.component.css'
})
export class GenerateMysteryComponent implements OnInit {
  mysteryForm!: FormGroup
  mystery$!: Promise<Mystery>
  theme: string = 'Generate a random theme'
  message: string = ''
  userId: string = ''
  

  
  @Output() 
  mysteryGenerated = new EventEmitter<any>();

  private fb = inject(FormBuilder)
  private gameService = inject(GameService)
  private store = inject(Store<AppState>)

  ngOnInit(): void {
    this.createMysteryForm()
    
  }

  createMysteryForm() {
    this.mysteryForm = this.fb.group({
      theme: this.fb.control<string>('Generate a random theme')
    })
  }

  generateMystery() {
    // extract form details
    const theme = this.mysteryForm.get('theme')?.value || this.theme
    const user = sessionStorage.getItem('user')
    if (user) {
      this.userId = JSON.parse(user).id
    }
    console.info('Theme:', theme)
    console.info('UserId:', this.userId)
    
    // check if api key is present
    console.info('Checking API key...')
    this.store.dispatch(checkApiKey())

    this.store.select(selectApiKeyPresent).pipe(take(5)).subscribe(apiKeyPresent => {
      console.info('Api key present:', apiKeyPresent)
      if (apiKeyPresent) {
        console.info('Generating mystery...')
        this.message = 'Generating mystery... (takes around 2 mins!)'
        this.store.dispatch(addMystery({ theme: theme, userId: this.userId }))
      } else {
        this.message = 'Input a valid API key.'
      }
    })

    // check if mystery is successfully generated
    this.store.select(selectAddMysterySuccess).pipe(take(5)).subscribe(success => {
      if (success) {
          console.info('Successfully generated mystery')
          this.message = ''
          this.mysteryGenerated.emit();
          this.mysteryForm.reset();
          document.getElementById("closeGenerateModal")?.click();
      }
    })

    // listen for errors
    this.store.select(selectMysteryError).pipe(take(5)).subscribe(error => {
      if (error) {
        this.message = error
      }
    })

  }


}


