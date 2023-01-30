import SpotifyApi from "../api/spotify-api";
import {useUserContext} from "../context/userContext";

export default function useSpotifyApi(): SpotifyApi {
    const {user} = useUserContext();

    return new SpotifyApi(user?.providerAccessToken ?? "");
}