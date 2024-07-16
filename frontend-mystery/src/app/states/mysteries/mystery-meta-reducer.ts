import { ActionReducer, MetaReducer } from "@ngrx/store";
import { addMysterySuccess, removeMysterySuccess } from "./mystery.actions"

// update session storage on each delete
export function sessionStorageMetaReducer(reducer: ActionReducer<any>): ActionReducer<any> {
    return (state, action) => {
        const nextState = reducer(state, action)
        if (action.type === removeMysterySuccess.type || action.type === addMysterySuccess.type) {
            const mysteries = nextState.mysteries.mysteries
            if (mysteries) {
                sessionStorage.setItem('mysteries', JSON.stringify(mysteries))
            }
        }
        return nextState;
    }
}

export const metaReducers: MetaReducer<any>[] = [sessionStorageMetaReducer]