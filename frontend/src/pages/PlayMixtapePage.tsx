import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useCallback, useEffect, useState} from "react";
import StorageKey from "../utils/local-storage-utils";
import {
    Box,
    Container,
    List, ListItem,
    Typography
} from "@mui/material";
import PageHeader from "../components/PageHeader";
import FlippableTrackCard from "../components/FlippableTrackCard";
import {
    usePlaybackState,
    usePlayerDevice,
    useSpotifyPlayer,
} from "react-spotify-web-playback-sdk";
import useSpotifyApi from "../hooks/useSpotifyApi";
import Track from "../types/track";
import PlayerTrackView from "../components/PlayerTrackView";
import useOpenClose from "../hooks/useOpenClose";
import {
    Info as InfoIcon,
} from "@mui/icons-material";
import FlippableImageCard from "../components/FlippableImageCard";
import UserAvatar from "../components/UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";
import MiniPlayer from "../components/MiniPlayer";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const {isOpen: isTrackViewOpen, open: openTrackView, close: closeTrackView} = useOpenClose();

    const [currentTrack, setCurrentTrack] = useState<Track | null>(null);
    const [playedTracks, setPlayedTracks] = useState<Track[]>([]);

    const player = useSpotifyPlayer();
    const device = usePlayerDevice();
    const state = usePlaybackState();
    const spotifyApi = useSpotifyApi();

    const addTracks = useCallback((tracks: Track[], startIndex: number) => {
        const uris = tracks.map(track => track.providerUri);

        return spotifyApi?.addTracks(uris, uris[startIndex], device?.device_id ?? "");
    }, [spotifyApi, device]);

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!mixtape || !device || device.status === "not_ready") {
            return;
        }

        (async () => {
            addTracks(mixtape.tracks, 0);
        })();
    }, [device, mixtape, spotifyApi, addTracks])

    useEffect(() => {
        if (!state || !mixtape || state.paused) {
            return;
        }

        const playingUri = state.track_window.current_track.uri;
        const track = mixtape.tracks.find(track => track.providerUri === playingUri);

        if (track) {
            setCurrentTrack(track);

            const alreadyPlayed = playedTracks.findIndex(played => played.id === track.id) !== -1;
            if (!alreadyPlayed) {
                setPlayedTracks([...playedTracks, track]);
            }
        }
    }, [state, mixtape, playedTracks]);

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

            {mixtape && playedTracks &&
              <List sx={{display: "flex", flexDirection: "column", gap: 2, p: 0, width: "100%"}}>
                  {playedTracks.map((track, index) => (
                      <ListItem key={track.id} sx={{p: 0}}>
                          <Container
                              sx={{display: "flex", justifyContent: "space-between", alignItems: "center", p: 0}}>
                              <Typography variant="h1" component="h3" sx={{mr: 2}}>{index + 1}</Typography>
                              <FlippableTrackCard track={track} mixtape={mixtape} onImageClick={() => addTracks(mixtape.tracks || [], index)}/>
                          </Container>
                      </ListItem>
                  ))}
              </List>
            }

            {playedTracks.length !== mixtape.tracks.length &&
                <Typography sx={{display: "flex", alignItems: "center"}}>
                  <InfoIcon color="primary" sx={{mr: 1}}/>
                    {playedTracks.length === 0
                        ? "Start listening to this mixtape to reveal the tracks."
                        : "Continue listening to reveal the remaining tracks."
                    }
                </Typography>
            }

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

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}