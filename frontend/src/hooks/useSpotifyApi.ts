import SpotifyApi from "../api/spotify-api";
import {useMemo} from "react";
import {useAuthenticatedUser} from "../components/ProtectedRoutes";

export default function useSpotifyApi(): SpotifyApi|null {
    const {user} = useAuthenticatedUser();

    return useMemo(() => {
        if (!user) {
            return null;
        }

        return new SpotifyApi(user.accessToken)
    }, [user]);
}