import SpotifyApi from "../api/spotify-api";
import {useUserContext} from "../context/userContext";
import {useMemo} from "react";

export default function useSpotifyApi(): SpotifyApi|null {
    const {user} = useUserContext();

    return useMemo(() => {
        if (!user) {
            return null;
        }

        return new SpotifyApi(user.providerAccessToken)
    }, [user]);
}