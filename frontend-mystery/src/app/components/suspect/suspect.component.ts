import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, map, of, switchMap, take } from 'rxjs';
import { Character, Message } from '../../models/game';
import { Store } from '@ngrx/store';
import { AppState } from '../../states/app.states';
import { selectCharacterById } from '../../states/mysteries/mystery.selectors';
import { selectMessagesByCharacterId, selectSendMessageFailure, selectSendMessageSuccess } from '../../states/chats/chats.selectors';
import { sendMessage } from '../../states/chats/chat.actions';

@Component({
  selector: 'app-suspect',
  templateUrl: './suspect.component.html',
  styleUrl: './suspect.component.css'
})
export class SuspectComponent implements OnInit, AfterViewChecked {
  @ViewChild('chatContainer') private chatContainer!: ElementRef;
  chatForm!: FormGroup
  suspectId!: string
  suspectId$?: Observable<string | null>
  mysteryId!: string
  character$!: Observable<Character | undefined>
  messages$!: Observable<Message[] | undefined> 
  messageSent: boolean = false
  username: string = ''

  private store = inject(Store<AppState>)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly fb = inject(FormBuilder)

  ngOnInit(): void {
    this.suspectId$ = this.activatedRoute.paramMap.pipe(
      map(params => params.get('suspectId'))
      )

    this.activatedRoute.parent?.params.subscribe(params => {
      this.mysteryId = params['mysteryId']
    })
      
    this.character$ = this.suspectId$.pipe(
      switchMap(suspectId => {
        if (suspectId) {
          this.suspectId = suspectId
          this.messages$ = this.store.select(selectMessagesByCharacterId(this.suspectId))
          return this.store.select(selectCharacterById(this.mysteryId, this.suspectId))
        } else {
          return of (undefined)
        }
      })
    )
      
    this.createChatForm();
  }


  createChatForm() {
    this.chatForm = this.fb.group({
      text: this.fb.control<string>('', [Validators.required])
    })
  }

  sendMessage(text: string, suspectName: string) {
    this.messageSent = true

    if (this.chatForm.valid) {
      const user = sessionStorage.getItem('user')
      if (user) {
        this.username = JSON.parse(user).username
      }
      const message: Message = {
        sender: this.username,
        mysteryId: this.mysteryId,
        suspectId: this.suspectId,
        suspectName: suspectName,
        text: text
      }
      console.info('Message:', message)
      
      this.store.dispatch(sendMessage({ message }))

      this.store.select(selectSendMessageSuccess).pipe(
        take(5)).subscribe(success => {
        if (success) {
          this.chatForm.reset()
          this.messageSent = false
        }
      })


      this.store.select(selectSendMessageFailure).pipe(
        take(5)).subscribe(error => {
        if (error) {
          console.error('Error:', error)
          alert(error)
          this.messageSent = false
          this.chatForm.reset()
        }
      })
    }
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    try {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Could not scroll to bottom:', err);
    }
  }
  
}
