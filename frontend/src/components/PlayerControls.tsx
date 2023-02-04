import {IconButton} from "@mui/material";
import {Pause as PauseIcon, PlayArrow as PlayArrowIcon, SkipPrevious as SkipPreviousIcon} from "@mui/icons-material";
import React from "react";
import {usePlaybackState, useSpotifyPlayer} from "react-spotify-web-playback-sdk";

export default function PlayerControls({size}: {
    size: number | string
}) {
    const player = useSpotifyPlayer();
    const state = usePlaybackState();

    return (
        <>
            <IconButton color="primary" sx={{fontSize: size, p: 0}} onClick={() => player?.previousTrack()}>
                <SkipPreviousIcon fontSize="inherit"/>
            </IconButton>
            {state && state.paused
                ? <IconButton color="primary" sx={{fontSize: size, p: 0}} onClick={() => player?.resume()}>
                    <PlayArrowIcon fontSize="inherit"/>
                </IconButton>
                : <IconButton color="primary" sx={{fontSize: size, p: 0}} onClick={() => player?.pause()}>
                    <PauseIcon fontSize="inherit"/>
                </IconButton>
            }
        </>
    )
}