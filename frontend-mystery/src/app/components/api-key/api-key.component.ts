import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-api-key',
  templateUrl: './api-key.component.html',
  styleUrl: './api-key.component.css'
})
export class ApiKeyComponent implements OnInit {
  apiKeyForm!: FormGroup
  message: string = ''

  private readonly fb = inject(FormBuilder)
  private readonly gameService = inject(GameService)

  ngOnInit(): void {
    this.createApiKeyForm()
  }

  createApiKeyForm() {
    this.apiKeyForm = this.fb.group({
      apiKey: this.fb.control<string>('', [Validators.required])
    })
  }

  submitApiKey() {
    const apiKey = this.apiKeyForm.get('apiKey')?.value
    console.info('apiKey: ', apiKey)
    this.gameService.submitApiKey(apiKey)
      .then(result => {
        console.info('Result:', result)
        // this.message = 'Successfully submitted API key.'
        this.apiKeyForm.reset()
        console.info('Clicking close...')
        document.getElementById("closeApiKeyModal")?.click()
        
      })
      .catch(error => {
        console.info('Error:', error)
        this.message = 'Error submitting API key.'
      })
  }
}
