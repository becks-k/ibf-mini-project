import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject, Injectable } from "@angular/core";
import { GameService } from "../../services/game.service";
import { addMystery, addMysteryFailure, addMysterySuccess, checkApiKey, checkApiKeyFailure, checkApiKeySuccess, loadMysteries, loadMysteriesFailure, loadMysteriesSuccess, removeMystery, removeMysteryFailure, removeMysterySuccess } from "./mystery.actions";
import { catchError, from, map, of, switchMap } from "rxjs";


@Injectable()
export class MysteryEffects {

    private actions$ = inject(Actions)
    private gameService = inject(GameService)

    loadMysteries$ = createEffect(() =>
        this.actions$.pipe(
            ofType(loadMysteries),
            switchMap(({ userId }) => {
                const cachedMysteries = sessionStorage.getItem('mysteries')
                if (cachedMysteries) {
                    // console.info('Mystery Effect: Retrieved mysteries from session storage')
                    return of(loadMysteriesSuccess({ mysteries: JSON.parse(cachedMysteries) }))
                } else {
                    return this.gameService.getMysteries(userId).pipe(
                        map(mysteries => {
                            // console.info('Mystery Effect: Retrieved mysteries from server')
                            sessionStorage.setItem('mysteries', JSON.stringify(mysteries))
                            return loadMysteriesSuccess({ mysteries })
                        }),
                        catchError((error => of(loadMysteriesFailure({ error: error.error.error }))))
                    )
                }
            }
        ))
    )

    deleteMystery$ = createEffect(() =>
        this.actions$.pipe(
            ofType(removeMystery),
            switchMap(({ id }) =>
                from(this.gameService.deleteMystery(id)).pipe(
                    map(() => removeMysterySuccess({ id })),
                    catchError((error) => of(removeMysteryFailure({ error: error.error.error})))
                ))
        ))

        addMystery$ = createEffect(() =>
            this.actions$.pipe(
                ofType(addMystery),
                switchMap(({ theme, userId }) =>
                    this.gameService.generateMystery(theme, userId).pipe(
                        map(mystery => addMysterySuccess({ mystery })),
                        catchError(error => {
                            console.error('Error log:', error)
                            let errorMsg = 'Error generating mystery. Please ensure API key is valid.'
                            return of(addMysteryFailure({ error: errorMsg }))
                        })
                    )
                )
            )
        )


    checkApiKey$ = createEffect(() =>
        this.actions$.pipe(
            ofType(checkApiKey),
            switchMap(() => 
                this.gameService.getApiKey().pipe(
                    map(() => checkApiKeySuccess()),
                    catchError(error => {
                        console.error('Error:', error)
                        const errorMsg = 'Please input a valid API key.'
                        return of(checkApiKeyFailure({ error: errorMsg }))
                    })
                ))
        ))

}