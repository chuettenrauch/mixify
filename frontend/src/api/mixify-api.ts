import User from "../types/user";
import axios from "axios";
import Mixtape from "../types/mixtape";
import Form from "../types/forms";
import FileMetadata from "../types/file-metadata";

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

    export async function createMixtape(mixtapeForm: Form.Mixtape): Promise<Mixtape> {
        const response = await client.post("", mixtapeForm);

        return response.data;
    }

    export async function deleteMixtape(mixtape: Mixtape): Promise<any> {
        const response = await client.delete(`/${mixtape.id}`);

        return response.data;
    }
}

export namespace FileApi {
    const client = axios.create({
        baseURL: "/api/files"
    })

    export async function uploadFile(file: File): Promise<FileMetadata> {
        const formData: FormData = new FormData();
        formData.set("file", file);

        const response = await client.post("", formData);

        return response.data;
    }
}
