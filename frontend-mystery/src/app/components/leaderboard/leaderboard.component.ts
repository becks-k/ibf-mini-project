import { Component, inject, OnInit } from '@angular/core';
import { GameService } from '../../services/game.service';
import { PlayerStat } from '../../models/game';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit {
  players$!: Promise<PlayerStat[]>

  private gameService = inject(GameService)
  
  ngOnInit(): void {
    this.players$ = this.gameService.getLeaderboard()
  }

}
