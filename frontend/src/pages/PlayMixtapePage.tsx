import {useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import React, {useCallback, useEffect, useState} from "react";
import {
    getLastPlayedTrackForMixtape,
    getLastReachedTrackForMixtape,
    setLastPlayedTrackForMixtape,
    setLastReachedTrackForMixtape,
    updateLastPlayUrlInLocalStorage
} from "../utils/local-storage-utils";
import {
    Box,
    Container,
    Typography
} from "@mui/material";
import PageHeader from "../components/PageHeader";
import {
    usePlaybackState,
    usePlayerDevice,
    useSpotifyPlayer,
} from "react-spotify-web-playback-sdk";
import useSpotifyApi from "../hooks/useSpotifyApi";
import Track from "../types/track";
import PlayerTrackView from "../components/PlayerTrackView";
import useOpenClose from "../hooks/useOpenClose";
import FlippableImageCard from "../components/FlippableImageCard";
import UserAvatar from "../components/UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";
import MiniPlayer from "../components/MiniPlayer";
import PlayedTracksList from "../components/PlayedTracksList";
import {useGlobalConfig} from "../context/globalConfigContext";

export default function PlayMixtapePage() {
    const location = useLocation();
    const globalConfig = useGlobalConfig();

    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const {isOpen: isTrackViewOpen, open: openTrackView, close: closeTrackView} = useOpenClose();

    const [currentTrack, setCurrentTrack] = useState<Track | null>(null);

    const player = useSpotifyPlayer();
    const device = usePlayerDevice();
    const state = usePlaybackState();
    const spotifyApi = useSpotifyApi();

    const addTracks = useCallback((tracks: Track[], startIndex: number) => {
        const uris = tracks.map(track => track.providerUri);
console.log("add tracks in play page", spotifyApi)
        return spotifyApi?.addTracks(uris, uris[startIndex], device?.device_id ?? "");
    }, [spotifyApi, device]);

    // automatically reload, if connection to spotify did not work
    useEffect(() => {
        const timeoutId = setTimeout(() => {
            if (globalConfig.canUsePlayer && device === null) {
                window.location.reload();
            }
        }, 3000);

        return () => clearTimeout(timeoutId);
    }, [device, globalConfig.canUsePlayer]);

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!globalConfig.canUsePlayer || !mixtape || !device || device.status === "not_ready") {
            return;
        }

        (async () => {
            const lastPlayedTrack = getLastPlayedTrackForMixtape(mixtape);
            addTracks(mixtape.tracks, lastPlayedTrack);
        })();
    }, [device, mixtape, spotifyApi, addTracks, globalConfig.canUsePlayer])

    useEffect(() => {
        if (!state || !mixtape || state.paused) {
            return;
        }

        const playingUri = state.track_window.current_track.uri;
        const track = mixtape.tracks.find(track => track.providerUri === playingUri);

        if (track) {
            setCurrentTrack(track);

            const index = mixtape.tracks.indexOf(track);
            setLastPlayedTrackForMixtape(mixtape, index);
            setLastReachedTrackForMixtape(mixtape, index);
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
            mb: 10,
        }}>
            <PageHeader title={mixtape.title}/>

            <FlippableImageCard image={{src: mixtape.imageUrl, alt: mixtape.title}} textOnBack={mixtape.description}/>

            <Box sx={{display: "flex", gap: 1, width: "100%"}}>
                <UserAvatar user={mixtape.createdBy} sx={{width: 60, height: 60}}/>
                <Box sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography variant="h2" textTransform="uppercase">by {mixtape.createdBy.name}</Typography>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                    <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                </Box>
            </Box>

            <PlayedTracksList
                mixtape={mixtape}
                playedTracks={mixtape.tracks.slice(0, getLastReachedTrackForMixtape(mixtape) + 1)}
                onTrackPlay={(trackIndex: number) => addTracks(mixtape.tracks || [], trackIndex)}
            />

            {currentTrack && state &&
              <MiniPlayer track={currentTrack} onClick={openTrackView}/>
            }

            {currentTrack &&
              <PlayerTrackView
                open={isTrackViewOpen && !!currentTrack}
                mixtape={mixtape}
                track={currentTrack}
                ready={!!device}
                onClose={() => closeTrackView()}
              />
            }

        </Container>
    );
}
