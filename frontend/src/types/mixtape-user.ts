import User from "./user";
import Mixtape from "./mixtape";

type MixtapeUser = {
    id: string,
    user: User,
    mixtape: Mixtape
}

export default MixtapeUser;