import User from "./user";

type Mixtape = {
    id: string,
    title: string,
    description: string,
    image: string,
    createdAt: string,
    createdBy: User
}

export default Mixtape;