import {Box, IconButton, LinearProgress, Typography} from "@mui/material";
import {PauseCircle as PauseCircleIcon, SkipPrevious as SkipPreviousIcon} from "@mui/icons-material";
import FlippableImageCard from "./FlippableImageCard";
import React, {useEffect, useState} from "react";
import Track from "../types/track";
import {usePlaybackState} from "react-spotify-web-playback-sdk";
import formatDuration from "format-duration";

export default function PlayerTrackView({track, ready, onPause, onPrevious}: {
    track: Track,
    ready: boolean,
    onPause: () => void,
    onPrevious: () => void,
}) {
    const state = usePlaybackState(true, 1000);

    const [progress, setProgress] = useState<number>(0);

    useEffect(() => {
        if (!state) {
            return;
        }

        const progress = state.position / state.duration * 100;
        setProgress(progress);
    }, [state])

    return (
        <>
            <FlippableImageCard image={{src: track.imageUrl, alt: track.name}} textOnBack={track.description}/>

            <Box sx={{width: "100%"}}>
                <Box sx={{textAlign: "left", width: "100%", mb: 1}}>
                    <Typography variant="h2" textTransform="uppercase">{track.name}</Typography>
                    <Typography>{track.artist}</Typography>
                </Box>

                <LinearProgress variant="determinate" value={progress}/>
                {state &&
                  <Box sx={{display: "flex", justifyContent: "space-between"}}>
                    <Typography>{formatDuration(state.position)}</Typography>
                    <Typography>-{formatDuration(state.duration - state.position)}</Typography>
                  </Box>
                }

                {ready &&
                  <Box sx={{display: "flex", justifyContent: "center"}}>
                    <IconButton sx={{fontSize: 100, p: 0}} onClick={onPrevious}>
                      <SkipPreviousIcon fontSize="inherit"/>
                    </IconButton>
                    <IconButton sx={{fontSize: 100, p: 0}} onClick={onPause}>
                      <PauseCircleIcon fontSize="inherit"/>
                    </IconButton>
                  </Box>
                }
            </Box>
        </>
    )
}