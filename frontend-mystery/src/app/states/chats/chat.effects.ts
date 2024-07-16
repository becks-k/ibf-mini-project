import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { GameService } from "../../services/game.service";
import { sendMessage, sendMessageFailure, sendMessageSuccess } from "./chat.actions";
import { catchError, map, of, switchMap } from "rxjs";

@Injectable()
export class ChatEffects {

    private actions$ = inject(Actions)
    private gameService = inject(GameService)

    addMessage$ = createEffect(() =>
        this.actions$.pipe(
            ofType(sendMessage),
            switchMap(({ message }) => {
                return this.gameService.chat(message).pipe(
                    map(message => sendMessageSuccess({ message: message })),
                    catchError(error => {
                        console.error('Error:', error)
                        const errorMsg = 'Please input a valid OpenAI API key'
                        return of(sendMessageFailure({ error: errorMsg }))
                    })
                )
            }   
        )
    ))
}
