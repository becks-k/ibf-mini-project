import { createSelector } from "@ngrx/store"
import { MysteryState } from "./mystery.reducers"
import { AppState } from "../app.states"

export const selectMysteryState = (state: AppState) => state.mysteries

export const selectAllMysteries = createSelector(
    selectMysteryState,
    (state: MysteryState) => state.mysteries
)

export const selectMysteryById = (mysteryId: string) => createSelector(
    selectMysteryState,
    (state: MysteryState) => state.mysteries.find(mystery => mystery.mysteryId === mysteryId)
)

export const selectCharacterById = (mysteryId: string, charId: string) => createSelector(
    selectMysteryState,
    (state: MysteryState) => {
        const mystery = state.mysteries.find(mystery => mystery.mysteryId === mysteryId)
        return mystery ? mystery.characters.find(character => character.id === charId) : undefined
    }
)

export const selectAddMysterySuccess = createSelector(
    selectMysteryState,
    (state: MysteryState) => state.addMysteryStatus === 'success'
)

// export const selectAddMysteryFailure = createSelector(
//     selectMysteryState,
//     (state: MysteryState) => state.addMysteryStatus === 'error'
// )

export const selectMysteryError = createSelector(
    selectMysteryState,
    (state: MysteryState) => state.error
)

export const selectApiKeyPresent = createSelector(
    selectMysteryState,
    (state: MysteryState) => state.apiKeyPresent
)



