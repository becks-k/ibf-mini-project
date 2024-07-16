import { createSelector } from "@ngrx/store";
import { AppState } from "../app.states";
import { ChatState } from "./chats.reducers";

export const selectChatState = (state: AppState) => state.chats

export const selectMessagesByCharacterId = (charId: string) => createSelector(
    selectChatState,
    (state: ChatState) => state ? state.messages.filter(message => message.suspectId === charId) : []
)

export const selectSendMessageSuccess = createSelector(
    selectChatState,
    (state: ChatState) => state.status === 'success'
)

export const selectSendMessageFailure = createSelector(
    selectChatState,
    (state: ChatState) => state.error
)
