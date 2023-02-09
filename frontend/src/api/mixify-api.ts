import {AuthenticatedUser} from "../types/user";
import axios from "axios";
import Mixtape from "../types/mixtape";
import Form from "../types/forms";
import FileMetadata from "../types/file-metadata";
import Track from "../types/track";
import Invite from "../types/invite";
import Utils from "../utils/utils";

const client = axios.create({
    baseURL: "/api"
});

client.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        if (error.response) {
            if (error.response.status === 401) {
                window.location.href = Utils.createLoginLink();
            }
        }

        return Promise.reject(error);
    }
);


export namespace UserApi {
    export async function getAuthenticatedUser(): Promise<AuthenticatedUser> {
        const response = await client.get("/users/me");

        return response.data;
    }

    export async function logout() {
        return await client.post("/users/logout");
    }
}

export namespace MixtapeApi {
    export async function getMixtapes(): Promise<Mixtape[]> {
        const response = await client.get("/mixtapes");

        return response.data;
    }

    export async function createMixtape(mixtapeForm: Form.Mixtape): Promise<Mixtape> {
        const response = await client.post("/mixtapes", mixtapeForm);

        return response.data;
    }

    export async function getMixtape(id: string): Promise<Mixtape> {
        const response = await client.get(`/mixtapes/${id}`);

        return response.data;
    }

    export async function deleteMixtape(mixtape: Mixtape): Promise<any> {
        const response = await client.delete(`/mixtapes/${mixtape.id}`);

        return response.data;
    }

    export async function updateMixtape(mixtape: Mixtape): Promise<Mixtape> {
        const response = await client.put(`/mixtapes/${mixtape.id}`, mixtape);

        return response.data;
    }
}

export namespace TrackApi {
    export async function createTrack(mixtape: Mixtape, trackForm: Form.Track): Promise<Track> {
        const response = await client.post(`/mixtapes/${mixtape.id}/tracks`, trackForm);

        return response.data;
    }

    export async function deleteTrack(mixtape: Mixtape, track: Track): Promise<any> {
        const response = await client.delete(`/mixtapes/${mixtape.id}/tracks/${track.id}`);

        return response.data;
    }

    export async function updateTrack(mixtape: Mixtape, track: Track): Promise<Track> {
        const response = await client.put(`/mixtapes/${mixtape.id}/tracks/${track.id}`, track);

        return response.data;
    }
}

export namespace FileApi {
    export async function uploadFile(file: File): Promise<FileMetadata> {
        const formData: FormData = new FormData();
        formData.set("file", file);

        const response = await client.post("/files", formData);

        return response.data;
    }
}

export namespace InviteApi {
    export async function createInvite(inviteForm: Form.Invite): Promise<Invite> {
        const response = await client.post("/invites", inviteForm);

        return response.data;
    }
}