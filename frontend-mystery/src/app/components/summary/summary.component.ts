import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Mystery } from '../../models/game';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { removeMystery } from '../../states/mysteries/mystery.actions';
import { selectAllMysteries } from '../../states/mysteries/mystery.selectors';



@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.css'
})
export class SummaryComponent implements OnInit {
  username: string = ''
  userId!: string
  mysteries$!: Observable<Mystery[]>
  selectedMystery!: Mystery
  generateMystery: boolean = false
  testUserId: string = '305e6b9d'

  private activatedRoute = inject(ActivatedRoute)
  private router = inject(Router)
  private store = inject(Store<AppState>)


  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      params => {
        this.userId = params['userId']
        this.mysteries$ = this.store.select(selectAllMysteries)

        const user = sessionStorage.getItem('user')
        if (user) {
          this.username = JSON.parse(user).username
        }
      }
    )
  }
  
  viewMystery(mystery: Mystery) {
    this.selectedMystery = mystery
    this.router.navigate([`/home/${this.userId}/${mystery.mysteryId}`])
  }

  deleteMystery(mysteryId: string) {
    console.info('Deleting mysteries...')
    this.store.dispatch(removeMystery({ id: mysteryId }))
    this.mysteries$ = this.store.select(selectAllMysteries)
  }

  openGenerateModal() {
    this.generateMystery = true
  }

  onMysteryGenerated(event: any) {
    this.mysteries$ = this.store.select(selectAllMysteries)

  }

  playMystery(mystery: Mystery) {
    this.router.navigate(['/game', mystery.mysteryId])
  }


}
