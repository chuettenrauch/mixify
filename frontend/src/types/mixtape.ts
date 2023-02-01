import User from "./user";
import Track from "./track";
import Form from "./forms";

type Mixtape = Form.Mixtape & {
    id: string,
    createdAt: string,
    createdBy: User,
    tracks: Track[],
}

export default Mixtape;