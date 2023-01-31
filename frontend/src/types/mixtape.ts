import User from "./user";
import Track from "./track";

type Mixtape = {
    id: string,
    title: string,
    description: string,
    imageUrl: string,
    createdAt: string,
    createdBy: User,
    tracks: Track[],
}

export default Mixtape;