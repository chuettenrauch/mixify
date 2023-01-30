import axios, {Axios} from "axios";

class SpotifyApi {
    private client: Axios;

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
}

export default SpotifyApi;