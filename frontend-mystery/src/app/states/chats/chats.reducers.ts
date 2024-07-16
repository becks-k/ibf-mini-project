import { createReducer, on } from "@ngrx/store";
import { Message } from "../../models/game";
import { sendMessage, sendMessageFailure, sendMessageSuccess } from "./chat.actions";

export interface ChatState {
    messages: Message[]
    error: string | null
    status: string
}

export const initialState: ChatState = {
    messages: [],
    error: null,
    status: 'pending'
}

export const chatReducer = createReducer(
    initialState,
    on(sendMessage, (state, { message }) => ({
        ...state, 
        messages: [...state.messages, message],
        error: null,
        status: 'pending'
    })),
    on(sendMessageSuccess, (state, { message }) => ({
        ...state,
        messages:[...state.messages, message],
        error: null,
        status: 'success'
    })),
    on(sendMessageFailure, (state, { error }) => ({
        ...state,
        error: error,
        status: 'error'
    }))
)
