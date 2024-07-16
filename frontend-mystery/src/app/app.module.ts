import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './components/landing/landing.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { InstructionsComponent } from './components/instructions/instructions.component';
import { authGuard } from './guards/auth.guard';
import { authInterceptor } from './interceptors/auth.interceptor';
import { SummaryComponent } from './components/summary/summary.component';
import { HeaderComponent } from './components/header/header.component';
import { GameComponent } from './components/game/game.component';
import { MysteryDetailComponent } from './components/mystery-detail/mystery-detail.component';
import { GenerateMysteryComponent } from './components/generate-mystery/generate-mystery.component';
import { NgPipesModule } from 'ngx-pipes';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ApiKeyComponent } from './components/api-key/api-key.component';
import { SuspectComponent } from './components/suspect/suspect.component';
import { ArticleComponent } from './components/article/article.component';
import { AccuseComponent } from './components/accuse/accuse.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { StoreModule } from '@ngrx/store';
import { OutcomeComponent } from './components/outcome/outcome.component';
import { CrimeSceneComponent } from './components/crime-scene/crime-scene.component';
import { ResetPasswordLinkComponent } from './components/reset-password-link/reset-password-link.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { EffectsModule } from '@ngrx/effects';
import { MysteryEffects } from './states/mysteries/mystery.effects';
import { mysteryReducer } from './states/mysteries/mystery.reducers';
import { metaReducers } from './states/mysteries/mystery-meta-reducer';
import { chatReducer } from './states/chats/chats.reducers';
import { ChatEffects } from './states/chats/chat.effects';
import { ServiceWorkerModule } from '@angular/service-worker';
import { LeaderboardComponent } from './components/leaderboard/leaderboard.component';

const appRoutes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'reset-password', component:  ResetPasswordComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'home/:userId', component: HomeComponent, canActivate: [authGuard] },
  { path: 'home/:userId/:mysteryId', component: MysteryDetailComponent, canActivate: [authGuard] },
  { path: 'game/:mysteryId', component: GameComponent, canActivate: [authGuard], children: [
    { path: 'crime', component: CrimeSceneComponent },
    { path: 'suspect/:suspectId', component: SuspectComponent },
    { path: 'article', component: ArticleComponent },
    { path: 'accuse', component: AccuseComponent },
  ]},
  { path: 'game/:mysteryId/outcome', component: OutcomeComponent, canActivate: [authGuard] },
  { path: 'leaderboard', component: LeaderboardComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '', pathMatch: 'full' }
]
@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    InstructionsComponent,
    SummaryComponent,
    HeaderComponent,
    GameComponent,
    MysteryDetailComponent,
    GenerateMysteryComponent,
    ApiKeyComponent,
    SuspectComponent,
    ArticleComponent,
    AccuseComponent,
    SidebarComponent,
    OutcomeComponent,
    CrimeSceneComponent,
    ResetPasswordLinkComponent,
    ResetPasswordComponent,
    LeaderboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    NgPipesModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
    StoreModule.forRoot({ mysteries: mysteryReducer, chats: chatReducer }, { metaReducers }),
    EffectsModule.forRoot([MysteryEffects, ChatEffects]),
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
