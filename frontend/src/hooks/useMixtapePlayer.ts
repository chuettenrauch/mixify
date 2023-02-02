import {usePlayerDevice, useSpotifyPlayer, useWebPlaybackSDKReady} from "react-spotify-web-playback-sdk";
import useSpotifyApi from "./useSpotifyApi";
import {useCallback, useEffect, useState} from "react";

export default function useMixtapePlayer() {
    const [playerReady, setPlayerReady] = useState<boolean>(false);

    const webPlaybackSDKReady = useWebPlaybackSDKReady();
    const player = useSpotifyPlayer();
    const device = usePlayerDevice();
    const spotifyApi = useSpotifyApi();

    const addTracksToPlayer = useCallback(async (uris: string[]) => {
        await spotifyApi?.addTracks(uris, device?.device_id ?? "");
    }, [spotifyApi, device]);

    useEffect(() => {
        if (webPlaybackSDKReady && spotifyApi && device && device.status === "ready") {
            setPlayerReady(true);
        }
    }, [webPlaybackSDKReady, spotifyApi, device, setPlayerReady]);

    const startPlayer = useCallback(async () => {
        return player?.resume();
    }, [player]);

    const pausePlayer = useCallback(async () => {
        return player?.pause();
    }, [player]);

    return {playerReady, addTracksToPlayer, startPlayer, pausePlayer};
}