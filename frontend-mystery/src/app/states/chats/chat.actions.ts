import { createAction, props } from "@ngrx/store";
import { Message } from "../../models/game";

export const sendMessage = createAction(
    '[Message] Send Message',
    props<{ message: Message }>()
)

export const sendMessageSuccess = createAction(
    '[Message] Send Message Success',
    props<{ message: Message }>()
)

export const sendMessageFailure = createAction(
    '[Message] Send Message Failure',
    props<{ error: string }>()
)

