import { ChatState } from "./chats/chats.reducers";
import { MysteryState } from "./mysteries/mystery.reducers";

export interface AppState {
    mysteries: MysteryState
    chats: ChatState
}