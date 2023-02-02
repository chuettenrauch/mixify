import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useEffect} from "react";
import StorageKey from "../utils/local-storage-utils";
import useMixtapePlayer from "../hooks/useMixtapePlayer";
import {Box, CardMedia, Container, IconButton, Typography} from "@mui/material";
import PageHeader from "../components/PageHeader";
import {PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import UserAvatar from "../components/UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const {playerReady, addTracksToPlayer, startPlayer, pausePlayer} = useMixtapePlayer();

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!playerReady || !mixtape) {
            return;
        }

        (async() => {
            const uris = mixtape.tracks.map(track => track.providerUri);
            await addTracksToPlayer(uris)
        })();
    }, [playerReady, addTracksToPlayer, mixtape])

    // ensure that player is stopped, if user leaves the page
    useEffect(() => {
        return () => {
            (async () => {
                await pausePlayer();
            })();
        }
    }, [pausePlayer])

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

            <Box sx={{
                height: 0,
                overflow: "hidden",
                width: "100%",
                paddingTop: "100%",
                position: "relative",
                border: "1px solid grey",
            }}>
                <CardMedia
                    component="img"
                    image={mixtape.imageUrl}
                    alt={mixtape.title}
                    sx={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                        objectFit: "cover",
                        width: "100%",
                        height: "100%"
                    }}
                />
                {playerReady &&
                  <IconButton size="large" component="button" onClick={() => startPlayer()} aria-label="play" sx={{
                      display: "block",
                      m: 0,
                      position: "absolute",
                      top: 0,
                      left: 0,
                      width: "100%",
                      height: "100%",
                      zIndex: 1
                  }}>
                    <PlayCircleIcon sx={{width: "70%", height: "100%"}}/>
                  </IconButton>
                }
            </Box>

            <Box sx={{display: "flex", gap: 1, width: "100%"}}>
                <UserAvatar user={mixtape.createdBy} sx={{width: 60, height: 60}}/>
                <Box sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography variant="h2">by {mixtape.createdBy.name}</Typography>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                </Box>
            </Box>

            {playerReady &&
              <>
                <button onClick={() => pausePlayer()}>Pause</button>
              </>
            }
        </Container>
    );
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}