import { createAction, props } from "@ngrx/store";
import { Mystery } from "../../models/game";

// add mystery
export const addMystery = createAction(
    '[Mystery] Add Mystery',
    props<{theme: string, userId: string}>()
)

export const addMysterySuccess = createAction(
    '[Mystery API] Add Mystery Success',
    props<{ mystery: Mystery }>()
);

export const addMysteryFailure = createAction(
    '[Mystery API] Add Mystery Failure',
    props<{ error: string }>()
);

// delete mystery
export const removeMystery = createAction(
    '[Mystery] Remove Mystery',
    props<{id: string}>()
)

export const removeMysterySuccess = createAction(
    '[Mystery API] Remove Mystery Success',
    props<{id: string}>()
)

export const removeMysteryFailure = createAction(
    '[Mystery API] Remove Mystery Failure',
    props<{error: string}>()
)

// load mysteries
export const loadMysteries = createAction(
    '[Mystery] Load Mysteries',
    props<{ userId: string }>()
);

export const loadMysteriesSuccess = createAction(
    '[Mystery API] Mysteries Load Success',
    props<{mysteries: Mystery[]}>()
)

export const loadMysteriesFailure = createAction(
    '[Mystery API] Mysteries Load Failure',
    props<{error: string}>()
)

// check if API key is cached
export const checkApiKey = createAction('[Mystery API] Check API Key')

export const checkApiKeySuccess = createAction('[Mystery API] Check API Key Success')

export const checkApiKeyFailure = createAction(
    '[Mystery API] Check API Key Failure',
    props<{ error: string }>()
)

