import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useEffect, useState} from "react";
import StorageKey from "../utils/local-storage-utils";
import {
    Box,
    Container, IconButton,
    List, ListItem,
    Typography
} from "@mui/material";
import PageHeader from "../components/PageHeader";
import SimpleTrackCard from "../components/SimpleTrackCard";
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
    PlayCircle as PlayCircleIcon,
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

    const startPlayer = async () => {
        return player?.resume();
    };

    const pausePlayer = async () => {
        closeTrackView();
        return player?.pause();
    };

    const playPreviousTrack = async () => {
        return player?.previousTrack();
    };

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!mixtape || !device || device.status === "not_ready") {
            return;
        }

        (async () => {
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

            const alreadyPlayed = playedTracks.findIndex(played => played.id === track.id) !== -1;
            if (!alreadyPlayed) {
                setPlayedTracks([...playedTracks, track]);
            }
        }
    }, [state, mixtape]);

    // ensure that player is stopped, if user leaves the page
    useEffect(() => {
        return () => {
            pausePlayer();
        }
    }, [player])

    const onMixtapePlay = () => {
        startPlayer();
        openTrackView();
    }

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

            <FlippableImageCard image={{src: mixtape.imageUrl, alt: mixtape.title}}
                                textOnBack={mixtape.description}>
                {device && state && state.paused &&
                  <IconButton size="large" component="button" onClick={onMixtapePlay} aria-label="play"
                              sx={{
                                  display: "block",
                                  m: 4,
                                  position: "absolute",
                                  top: 0,
                                  left: 0,
                                  width: "80%",
                                  height: "80%",
                                  zIndex: 1,
                              }}>
                    <PlayCircleIcon sx={{width: "70%", height: "100%"}}/>
                  </IconButton>
                }
            </FlippableImageCard>

            <Box sx={{display: "flex", gap: 1, width: "100%"}}>
                <UserAvatar user={mixtape.createdBy} sx={{width: 60, height: 60}}/>
                <Box sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography variant="h2" textTransform="uppercase">by {mixtape.createdBy.name}</Typography>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                    <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                </Box>
            </Box>

            {playedTracks &&
              <List sx={{display: "flex", flexDirection: "column", gap: 2, p: 0, width: "100%"}}>
                  {playedTracks.map((track, index) => (
                      <ListItem key={track.id} sx={{p: 0}}>
                          <Container
                              sx={{display: "flex", justifyContent: "space-between", alignItems: "center", p: 0}}>
                              <Typography variant="h1" component="h3" sx={{mr: 2}}>{index + 1}</Typography>
                              <SimpleTrackCard track={track}/>
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

            {currentTrack && state && !state.paused &&
              <MiniPlayer track={currentTrack} onPrevious={playPreviousTrack} onPause={pausePlayer} onClick={openTrackView}/>
            }

            {currentTrack &&
              <PlayerTrackView
                open={isTrackViewOpen && !!currentTrack}
                mixtape={mixtape}
                track={currentTrack}
                ready={!!device}
                onClose={() => closeTrackView()}
                onPause={() => pausePlayer()}
                onPrevious={() => playPreviousTrack()}
              />
            }

        </Container>
    );
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}