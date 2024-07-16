import { Component, Input, OnInit, inject } from '@angular/core';
import { GameService } from '../../services/game.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  @Input()
  userId!: string

  private gameService = inject(GameService)

  ngOnInit(): void {
    const user = sessionStorage.getItem('user')
    if (user) {
      this.userId = JSON.parse(user).id
    }
  }

  openInstructions() {
    document.getElementById("instructionsModal")?.click()
  }

  logout() {
    this.gameService.removeApiKey()
    .then(result => {
      // console.info('Result:', result)
      sessionStorage.removeItem("auth_token")
      sessionStorage.removeItem("user")
      sessionStorage.removeItem("mysteries")
      this.gameService.outcome = undefined
      })
      .catch(error => {
        console.error('Error:', error)
      })
  }
}
