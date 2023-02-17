import User from "./user";
import Track from "./track";

type Mixtape = {
    id: string,
    title: string,
    description: string | null,
    imageUrl: string,
    createdAt: string,
    createdBy: User,
    tracks: Track[],
    draft: boolean,
}

export default Mixtape;