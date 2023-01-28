import User from "./user";
import Track from "./track";

type Mixtape = {
    id: string,
    title: string,
    description: string,
    image: string,
    createdAt: string,
    createdBy: User,
    tracks: Track[],
}

export default Mixtape;