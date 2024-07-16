import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup
  errorMessage: string = ''

  private readonly fb = inject(FormBuilder)
  private readonly router = inject(Router)
  private readonly authService = inject(AuthService)

  ngOnInit(): void {
    this.createRegisterForm()
  }

  createRegisterForm() {
    this.registerForm = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.maxLength(100)]),
      username: this.fb.control<string>('', [Validators.required, Validators.maxLength(20)]),
      password: this.fb.control<string>('', [Validators.required, Validators.maxLength(20)])
    })
  }

  register() {
    const email = this.registerForm.get('email')?.value
    const username = this.registerForm.get('username')?.value
    const password = this.registerForm.get('password')?.value
    this.authService.register(email, username, password)
      .then(result => {
        // console.info('Result: ', result)
        alert('Registration successful!')
        this.registerForm.reset()
        this.router.navigate(['/login'])
      })
      .catch(error => {
        console.error('Error: ', error)
        this.errorMessage = error.error.error
      })
  }

}
