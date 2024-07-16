import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GameService } from '../../services/game.service';
import { Outcome } from '../../models/game';

@Component({
  selector: 'app-outcome',
  templateUrl: './outcome.component.html',
  styleUrl: './outcome.component.css'
})
export class OutcomeComponent implements OnInit {
  mysteryId!: string
  outcome!: Outcome
  userId!: string

  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly gameService = inject(GameService) 

  ngOnInit(): void {
    this.activatedRoute.parent?.params.subscribe(params => {
      this.mysteryId = params['mysteryId']

      const outcome = this.gameService.outcome
      if (outcome !== undefined) {
        this.outcome = outcome
      }
      
      // Test
      // const outcome = sessionStorage.getItem('outcome')
      // if (outcome) {
      //   this.outcome = JSON.parse(outcome)
      // }

    })
    const user = sessionStorage.getItem('user')
    if (user) {
      this.userId = JSON.parse(user).userId
    }
  }

}
