export interface Mystery {
    mysteryId: string
    userId: string
    theme: string
    artStyle: string
    mysteryName: string
    introduction: string
    newsHeadline: string
    victim: string
    guilty: string
    guiltyMotive: string
    weaponUsedInCrime: string
    hints: string[]
    crimeLocationDescription: string
    crimeSceneImageUrl: string
    coverImageDescription: string
    coverImageUrl: string
    weapons: Weapon[]
    characters: Character[]
    articles: Article[]
}

export interface Character {
    id: string
    name: string
    role: string
    imageDescription: string;
    imageUrl: string;
    personality: string;
    guilty: boolean;
    victim: boolean;
    dead: boolean;
}

export interface Weapon {
    id: string;
    weapon: string;
    description: string;
    isUsedInCrime: boolean
}

export interface Article {
    id: string;
    headline: string;
    content: string;
}

export interface MysterySlice {
    mysteries: Mystery[]
}

export type Message = {
    sender: string
    mysteryId: string
    suspectName: string
    suspectId: string
    text: string
}

export interface Observation {
    sender: string
    mysteryId: string
    text: string
}

export interface ChatSlice {
    messages: Message[]
}

export interface Outcome {
    judgeResponse: string
    caseOutcome: string
    aftermath: string
    score: number
}

export type Accuse = {
    guilty: string
    weaponUsedInCrime: string
    guiltyMotive: string
    mysteryId: string
    userId: string
}

export interface Response {
    result: string
}

export interface ApiKey {
    apiKey: string
}

export interface PlayerStat {
    id: string
    username: string
    totalScore: number
    mysteriesPlayed: number
    highestScore: number
}
