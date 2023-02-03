import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useEffect} from "react";
import StorageKey from "../utils/local-storage-utils";
import useMixtapePlayer from "../hooks/useMixtapePlayer";
import {
    Box,
    Container,
    IconButton, List, ListItem,
    Typography
} from "@mui/material";
import PageHeader from "../components/PageHeader";
import {
    PlayCircle as PlayCircleIcon
} from "@mui/icons-material";
import UserAvatar from "../components/UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";
import FlippableImageCard from "../components/FlippableImageCard";
import SwipeableEdgeDrawer from "../components/SwipeableEdgeDrawer";
import SimpleTrackCard from "../components/SimpleTrackCard";

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
            <FlippableImageCard image={{src: mixtape.imageUrl, alt: mixtape.title}} textOnBack={mixtape.description}>
                {playerReady &&
                  <IconButton size="large" component="button" onClick={() => startPlayer()} aria-label="play" sx={{
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
                </Box>
            </Box>

            {playerReady &&
              <>
                <button onClick={() => pausePlayer()}>Pause</button>
              </>
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