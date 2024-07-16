import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-reset-password-link',
  templateUrl: './reset-password-link.component.html',
  styleUrl: './reset-password-link.component.css'
})
export class ResetPasswordLinkComponent implements OnInit {
  resetForm!: FormGroup
  message: string = ''
  isFormSubmitted: boolean = false

  private readonly fb = inject(FormBuilder)
  private authService = inject(AuthService)
  private readonly router = inject(Router)

  ngOnInit(): void {
    this.createResetForm()
  }

  createResetForm() {
    this.resetForm = this.fb.group({
      username: this.fb.control<string>('', [Validators.required])
    })
  }

  submitForm() {
    this.isFormSubmitted = true
    const username = this.resetForm.get('username')?.value
    this.authService.checkIfUserExists(username)
      .then(result => {
        // console.info('Result:', result)
        this.isFormSubmitted = false
        this.message = 'Link sent! Please check your registered email.'
        this.resetForm.reset()
      })
      .catch(error => {
        console.error('Error:', error)
        this.isFormSubmitted = false
        this.message = 'User does not exist. Please register for a new account.'
      })
  }

}
