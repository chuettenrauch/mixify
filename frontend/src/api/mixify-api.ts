import User from "../types/user";
import axios from "axios";

export module UserApi {
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
