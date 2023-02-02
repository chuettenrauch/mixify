import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import React, {useEffect, useState} from "react";
import StorageKey from "../utils/local-storage-utils";
import useMixtapePlayer from "../hooks/useMixtapePlayer";
import {Box, Card, CardMedia, Container, IconButton, Typography} from "@mui/material";
import PageHeader from "../components/PageHeader";
import {Redo as RedoIcon, PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import UserAvatar from "../components/UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";
import ReactCardFlip from "react-card-flip";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const {playerReady, addTracksToPlayer, startPlayer, pausePlayer} = useMixtapePlayer();
    const [isImageFlipped, setIsImageFlipped] = useState<boolean>(false);

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

                <ReactCardFlip isFlipped={isImageFlipped} flipSpeedBackToFront={1} flipSpeedFrontToBack={1} containerStyle={{width: "100%"}}>
                    <Card elevation={10} sx={{display: "flex", flexDirection: "column", width: "100%", p: 2, pb: 0}}>
                        <Container sx={{paddingTop: "100%", position: "relative"}}>
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
                                    height: "100%",
                                    border: "1px solid grey",
                                }}
                            />
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
                        </Container>

                        <IconButton onClick={() => setIsImageFlipped(!isImageFlipped)} sx={{marginLeft: "auto"}}>
                            <RedoIcon/>
                        </IconButton>
                    </Card>

                    <Card elevation={10} square={true} sx={{display: "flex", flexDirection: "column", width: "100%", p: 2, pb: 0}}>
                        <Container sx={{paddingTop: "100%", position: "relative"}}>
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
                                    height: "100%",
                                    border: "1px solid grey",
                                    opacity: 0.3,
                                }}
                            />
                            <Box sx={{
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                position: "absolute",
                                top: 0,
                                left: 0,
                                width: "100%",
                                height: "100%",
                                p: 2,
                            }}>
                                <Typography variant="h2" textAlign="justify">{mixtape.description}</Typography>
                            </Box>
                        </Container>

                        <IconButton onClick={() => setIsImageFlipped(!isImageFlipped)} sx={{marginLeft: "auto"}}>
                            <RedoIcon/>
                        </IconButton>
                    </Card>
                </ReactCardFlip>


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
        </Container>
    );
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}