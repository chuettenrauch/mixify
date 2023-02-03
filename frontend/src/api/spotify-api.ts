import axios, {Axios} from "axios";

class SpotifyApi {
    private client: Axios;
    private alreadyAddedTracks: boolean = false;

    constructor(token: string) {
        this.client = axios.create({
            baseURL: "https://api.spotify.com/v1",
        });

        this.client.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }

    async searchTracks(q: string, limit: number = 10, page: number = 1): Promise<Spotify.Track[]> {
        const response = await this.client.get("/search", {
            params: {
                q: q,
                type: "track",
                market: "DE",
                limit: limit,
                offset: (page - 1) * limit,
            }
        });

        return response.data.tracks.items ?? [];
    }

    async addTracks(uris: string[], deviceId: string) {
        if (this.alreadyAddedTracks) {
            return;
        }

        const response = await this.client.put(
            `/me/player/play?device_id=${deviceId}`,
            {
                uris: uris,
            },
        );

        this.alreadyAddedTracks = true;

        setTimeout(() => {
            this.alreadyAddedTracks = false;
        }, 3000);

        return response;
    }
}

export default SpotifyApi;