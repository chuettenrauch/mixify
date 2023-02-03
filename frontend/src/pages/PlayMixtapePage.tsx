import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useEffect, useState} from "react";
import StorageKey from "../utils/local-storage-utils";
import {
    Container,
    List, ListItem,
    Typography
} from "@mui/material";
import PageHeader from "../components/PageHeader";
import SwipeableEdgeDrawer from "../components/SwipeableEdgeDrawer";
import SimpleTrackCard from "../components/SimpleTrackCard";
import {
    usePlaybackState,
    usePlayerDevice,
    useSpotifyPlayer,
} from "react-spotify-web-playback-sdk";
import useSpotifyApi from "../hooks/useSpotifyApi";
import Track from "../types/track";
import PlayerMixtapeView from "../components/PlayerMixtapeView";
import PlayerTrackView from "../components/PlayerTrackView";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const [currentTrack, setCurrentTrack] = useState<Track | null>();

    const player = useSpotifyPlayer();
    const device = usePlayerDevice();
    const state = usePlaybackState();
    const spotifyApi = useSpotifyApi();

    const startPlayer = async () => {
        return player?.resume();
    };

    const pausePlayer = async () => {
        setCurrentTrack(null);
        return player?.pause();
    };

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!mixtape || !device || device.status === "not_ready") {
            return;
        }

        (async() => {
            const uris = mixtape.tracks.map(track => track.providerUri);
            await spotifyApi?.addTracks(uris, device?.device_id ?? "");
        })();
    }, [device, mixtape])

    useEffect(() => {
        if (!state || !mixtape || state.paused) {
            return;
        }

        const playingUri = state.track_window.current_track.uri;
        const track = mixtape.tracks.find(track => track.providerUri === playingUri);

        if (track) {
            setCurrentTrack(track);
        }
    }, [state, mixtape]);

    // ensure that player is stopped, if user leaves the page
    useEffect(() => {
        return () => {
            player?.pause();
        }
    }, [player])

    if (!mixtape) {
        return null;
    }

    return (
        <Container sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
            p: 0,
        }}>
            <PageHeader title={mixtape.title}/>

            {currentTrack
                ? <PlayerTrackView track={currentTrack} ready={!!device} onPause={() => pausePlayer()} onPrevious={() => player?.previousTrack()}/>
                : <PlayerMixtapeView mixtape={mixtape} ready={!!device} onPlay={() => startPlayer()}/>
            }

            <SwipeableEdgeDrawer title="Show tracks" drawerBleeding={130}>
                <List sx={{display: "flex", flexDirection: "column", gap: 2, p: 0}}>
                    {mixtape.tracks.map((track, index) => (
                        <ListItem key={track.id} sx={{p: 0}}>
                            <Container sx={{display: "flex", justifyContent: "space-between", alignItems: "center", p: 0}}>
                                <Typography variant="h1" component="h3" sx={{mr: 2}}>{index + 1}</Typography>
                                <SimpleTrackCard track={track}/>
                            </Container>
                        </ListItem>
                    ))}
                </List>
            </SwipeableEdgeDrawer>
        </Container>
    );
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}