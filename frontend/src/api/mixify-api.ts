import User from "../types/user";
import axios from "axios";
import Mixtape from "../types/mixtape";

export namespace UserApi {
    const client = axios.create({
        baseURL: "/api/users"
    });

    export async function getAuthenticatedUser(): Promise<User> {
        const response = await client.get("/me");

        return response.data;
    }

    export async function logout() {
        return await client.post("/logout");
    }
}

export namespace MixtapeApi {
    const client = axios.create({
        baseURL: "/api/mixtapes"
    });

    export async function getMixtapes(): Promise<Mixtape[]> {
        const response = await client.get("");

        return response.data;
    }
}
