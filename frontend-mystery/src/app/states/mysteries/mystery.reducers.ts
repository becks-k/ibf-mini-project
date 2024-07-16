import { createReducer, on } from "@ngrx/store"
import { Mystery } from "../../models/game"
import { addMystery, addMysteryFailure, addMysterySuccess, checkApiKeyFailure, checkApiKeySuccess, loadMysteries, loadMysteriesFailure, loadMysteriesSuccess, removeMystery, removeMysteryFailure, removeMysterySuccess } from "./mystery.actions"

// type MysteryStatus = 'pending' | 'loading' | 'error' | 'success'

export interface MysteryState {
    mysteries: Mystery[]
    error: string | null 
    status: string
    addMysteryStatus: string
    removeMysteryStatus: string
    loadMysteryStatus: string
    apiKeyPresent: boolean
}

export const initialState: MysteryState = {
    mysteries: [],
    error: null,
    status: 'pending',
    addMysteryStatus: 'pending',
    removeMysteryStatus: 'pending',
    loadMysteryStatus: 'pending',
    apiKeyPresent: false
}

export const mysteryReducer = createReducer(
    initialState,
    on(addMystery,(state) => ({
        ...state,
        error: null,
        status: 'pending',
        addMysteryStatus: 'pending'
    })),
    on(addMysterySuccess, (state, { mystery }) => ({
        ...state, 
        mysteries: [...state.mysteries, mystery],
        error: null,
        status: 'success',
        addMysteryStatus: 'success'
    })),
    on(addMysteryFailure, (state, {error}) => ({
        ...state,
        error: error,
        status: 'error',
        addMysteryStatus: 'error'
    })),
    on(removeMystery, (state, { id }) => ({
        ...state, 
        mysteries: state.mysteries.filter(mystery => mystery.mysteryId !== id)
    })),
    on(removeMysterySuccess, (state, { id }) => ({
        ...state,
        mysteries: state.mysteries.filter(mystery => mystery.mysteryId !== id),
        error: null,
        status: 'success',
        removeMysteryStatus: 'success'
    })),
    on(removeMysteryFailure, (state, { error }) => ({
        ...state,
        error: error,
        status: 'error',
        removeMysteryStatus: 'error'
    })),
    on(loadMysteries, (state) => ({
        ...state,
        status: 'loading',
        loadMysteryStatus: 'pending'
    })),
    on(loadMysteriesSuccess, (state, { mysteries }) => ({
        ...state,
        mysteries: mysteries,
        error: null,
        status: 'success',
        loadMysteryStatus: 'success'
    })),
    on(loadMysteriesFailure, (state, { error }) => ({
        ...state,
        error: error,
        status: 'error',
        loadMysteryStatus: 'error'
    })),
    on(checkApiKeySuccess, (state) => ({
        ...state,
        error: null,
        status: 'success',
        apiKeyPresent: true,
    })),
    on(checkApiKeyFailure, (state, { error }) => ({
        ...state,
        error: error,
        status: 'error',
        apiKeyPresent: false,
    }))
    

)

