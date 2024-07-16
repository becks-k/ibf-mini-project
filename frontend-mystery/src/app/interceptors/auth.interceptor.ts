import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {

  const authService = inject(AuthService)
  const router = inject(Router)

  const token = authService.getAuthToken();
  let authReq = req;

  // clones request with authorization header
  if (token) {
    authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    })
  }

  // requests that have invalid authentication redirects user back to landing page
  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 403) {
        alert('Please login!')
        router.navigate([''])
      }
      return throwError(error)
    })
  )
  
}
