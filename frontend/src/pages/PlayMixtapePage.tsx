import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from  "react-secure-storage";
import {useEffect} from "react";
import StorageKey from "../utils/local-storage-utils";
import {usePlayerDevice, useSpotifyPlayer, useWebPlaybackSDKReady} from "react-spotify-web-playback-sdk";
import useSpotifyApi from "../hooks/useSpotifyApi";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const ready = useWebPlaybackSDKReady();
    const player = useSpotifyPlayer();
    const device = usePlayerDevice();

    const spotifyApi = useSpotifyApi();

    useEffect(() => {
        if (!player) {
            return;
        }

        return ensurePlayerIsPausedIfUserLeavesPage(player);
    }, [player]);

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!spotifyApi || !mixtape || !device || device.status !== "ready") {
            return;
        }

        (async() => {
            const uris = mixtape.tracks.map(track => track.providerUri);
            await spotifyApi?.queueTracks(uris, device.device_id);
        })();
    }, [ready, spotifyApi, device, mixtape]);

    return (
        <>
            <h1>Play mixtape</h1>
            {mixtape &&
              <p>{mixtape.title}</p>
            }
        </>
    );
}

function ensurePlayerIsPausedIfUserLeavesPage(player: Spotify.Player): () => void {
    return () => player.pause();
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}