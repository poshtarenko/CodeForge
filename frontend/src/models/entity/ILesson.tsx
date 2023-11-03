import {ILanguage} from "./ILanguage";
import {IParticipation} from "./IParticipation";

export interface ILesson {
    id: number,
    authorId: number,
    name: string,
    description: string,
    inviteCode: string,
    language: ILanguage,
    participations: IParticipation[]
}