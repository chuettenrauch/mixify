import Mixtape from "./mixtape";
import {AuthenticatedUser} from "./user";

export const DraftFilter = new Map<string, (mixtape: Mixtape, user: AuthenticatedUser | null) => boolean>([
    ["yes", (mixtape: Mixtape, _) => mixtape.draft],
    ["no", (mixtape: Mixtape, _) => !mixtape.draft],
])

export const CreatedByFilter = new Map<string, (mixtape: Mixtape, user: AuthenticatedUser | null) => boolean>([
    ["user", (mixtape: Mixtape, user: AuthenticatedUser | null) => user ? mixtape.createdBy.id === user.id : false],
    ["other", (mixtape: Mixtape, user: AuthenticatedUser | null) => user ? mixtape.createdBy.id !== user.id : false],
])

type Filter = (mixtape: Mixtape, user: AuthenticatedUser | null) => boolean
export default Filter;

