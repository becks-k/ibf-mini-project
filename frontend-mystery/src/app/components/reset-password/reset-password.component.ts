import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit{
  resetPasswordForm!: FormGroup
  message: string = ''
  token: string = ''

  private readonly fb = inject(FormBuilder)
  private authService = inject(AuthService)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly router = inject(Router)

  ngOnInit(): void {
    this.token = this.activatedRoute.snapshot.queryParams['token'] || ''
    console.info('Token:', this.token)
    this.createResetPasswordForm()
  }

  createResetPasswordForm() {
    this.resetPasswordForm = this.fb.group({
      password: this.fb.control<string>('', [Validators.required, Validators.maxLength(50)]),
      confirmPassword: this.fb.control<string>('', [Validators.required, Validators.maxLength(50)])
    })
  }

  resetPassword() {
    if (this.resetPasswordForm.valid) {
      const password = this.resetPasswordForm.get('password')?.value
      const confirmPassword = this.resetPasswordForm.get('confirmPassword')?.value
      if (password !== confirmPassword) {
        this.message = 'Passwords do not match'
        return
      }
      this.authService.resetPassword(this.token, password)
        .then(result => {
          // console.info('Result:', result)
          this.message = ''
          this.router.navigate(['/login'])
          
        })
        .catch(error => {
          console.error('Error:', error)
          this.message = 'Error updating password'
        })
    }
  }
}
