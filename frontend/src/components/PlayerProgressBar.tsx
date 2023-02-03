import React, {useEffect, useState} from "react";
import {Box, LinearProgress, Typography} from "@mui/material";
import {usePlaybackState} from "react-spotify-web-playback-sdk";
import formatDuration from "format-duration";

export default function PlayerProgressBar({showDuration = true}: {
    showDuration?: boolean
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

    if (!state) {
        return null;
    }

    return (
        <Box sx={{width: "100%"}}>
            <LinearProgress variant="determinate" value={progress}/>
            {showDuration &&
              <Box sx={{display: "flex", justifyContent: "space-between"}}>
                <Typography>{formatDuration(state.position)}</Typography>
                <Typography>-{formatDuration(state.duration - state.position)}</Typography>
              </Box>
            }
        </Box>
    );
}