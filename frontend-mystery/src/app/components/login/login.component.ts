
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup
  errorMessage: string = ''

  private readonly fb = inject(FormBuilder)
  private readonly router = inject(Router)
  private readonly authService = inject(AuthService)
  private gameService = inject(GameService)

  ngOnInit(): void {
    this.createLoginForm()
  }

  createLoginForm() {
    this.loginForm = this.fb.group({
      username: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', [Validators.required, Validators.maxLength(20)])
    })
  }

  login() {
    const username = this.loginForm.get('username')?.value
    const password = this.loginForm.get('password')?.value
    this.authService.login(username, password)
      .then(result => {
        this.authService.setAuthToken(result.token)
        sessionStorage.setItem('user', JSON.stringify(result))
        this.loginForm.reset()
        this.router.navigate(['/home', result.id])
      })
      .catch(error => {
        console.error('Error: ', error)
        this.errorMessage = error.error.error
      })
  }

}
