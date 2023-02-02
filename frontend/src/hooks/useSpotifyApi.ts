import SpotifyApi from "../api/spotify-api";
import {useUserContext} from "../context/userContext";

export default function useSpotifyApi(): SpotifyApi|null {
    const {user} = useUserContext();

    if (!user) {
        return null;
    }

    return new SpotifyApi(user?.providerAccessToken);
}