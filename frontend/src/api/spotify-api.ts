import axios, {AxiosInstance} from "axios";
import {UserApi} from "./mixify-api";

class SpotifyApi {
    private readonly client: AxiosInstance;
    private alreadyAddedTracks: boolean = false;
    private accessToken: string;

    constructor(accessToken: string) {
        this.client = axios.create({
            baseURL: "https://api.spotify.com/v1",
        });

        this.accessToken = accessToken;

        this.registerAccessTokenRequestInterceptor();
        this.registerRefreshTokenResponseInterceptor();
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

    async addTracks(uris: string[], startUri: string, deviceId: string) {
        if (this.alreadyAddedTracks) {
            return;
        }

        const response = await this.client.put(
            `/me/player/play?device_id=${deviceId}`,
            {
                uris: uris,
                offset: {
                    uri: startUri
                }
            },
        );

        this.alreadyAddedTracks = true;

        setTimeout(() => {
            this.alreadyAddedTracks = false;
        }, 3000);

        return response;
    }

    async refreshToken() {
        const authenticatedUser = await UserApi.getAuthenticatedUser();
        this.accessToken = authenticatedUser.providerAccessToken;
    }

    private registerAccessTokenRequestInterceptor() {
        this.client.interceptors.request.use(
            (config) => {
                config.headers["Authorization"] = `Bearer ${this.accessToken}`;
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        )
    }

    private registerRefreshTokenResponseInterceptor() {
        this.client.interceptors.response.use(
            (response) => {
                return response;
            },
            async (error) => {
                const requestConfig = error.config;

                if (error.response) {
                    if (error.response.status === 401 && !requestConfig.retry) {
                        requestConfig.retry = true;

                        try {
                            await this.refreshToken();

                            return this.client(requestConfig);
                        } catch (refreshError) {
                            return Promise.reject(refreshError);
                        }
                    }
                }

                return Promise.reject(error);
            }
        )
    }
}

export default SpotifyApi;