import {PauseCircle as PauseCircleIcon, PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import {IconButton} from "@mui/material";
import React, {useRef, useState} from "react";

export default function AudioButton({src, type = "audio/mpeg"}: {
    src: string,
    type?: string
}) {
    const player = useRef<HTMLAudioElement | null>(null);
    const [playing, setPlaying] = useState<boolean>(false);

    const onPlay = () => {
        if (player.current?.paused) {
            setPlaying(true);
            player.current?.play();
        } else {
            setPlaying(false);
            player.current?.pause();
        }
    }

    return (
        <IconButton
            size="large"
            onClick={onPlay}
        >
            <audio ref={player} hidden>
                <source src={src} type={type}/>
            </audio>
            {playing
                ? <PauseCircleIcon/>
                : <PlayCircleIcon/>
            }
        </IconButton>
    )
}