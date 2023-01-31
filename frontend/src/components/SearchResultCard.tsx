import {
    Box,
    Card,
    CardActionArea, CardActions,
    CardContent,
    CardMedia, IconButton,
    Typography
} from "@mui/material";
import React, {useRef, useState} from "react";
import {PlayCircle as PlayCircleIcon, PauseCircle as PauseCircleIcon} from "@mui/icons-material";
import formatDuration from "format-duration";

declare global {
    namespace Spotify {
        interface Track {
            preview_url: string;
        }
    }
}

export default function SearchResultCard({searchResult}: {
    searchResult: Spotify.Track,
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
        <Card elevation={5} sx={{display: "flex", p: 2}}>
            <CardActions sx={{p: 0, position: "relative"}}>
                    <IconButton
                        size="large"
                        onClick={onPlay}
                        sx={{
                            display: "block",
                            m: 0,
                            position: "absolute",
                            top: 0,
                            left: 0,
                            width: "100%",
                            height: "100%",
                            zIndex: 1
                        }}
                    >
                        <audio ref={player} hidden>
                            <source src={searchResult.preview_url} type="audio/mpeg"/>
                        </audio>
                        {playing
                            ? <PauseCircleIcon sx={{width: "100%", height: "100%"}}/>
                            : <PlayCircleIcon sx={{width: "100%", height: "100%"}}/>
                        }
                    </IconButton>
                    <CardMedia
                        component="img"
                        image={searchResult.album.images[0]?.url}
                        alt={searchResult.name}
                        sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey", opacity: 0.7}}
                    />
            </CardActions>

            <CardActionArea sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 0}}>
                <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "space-between", p: 0, pl: 2}}>
                    <Box>
                        <Typography variant="h3">{searchResult.name}</Typography>
                        <Typography>{searchResult.artists[0]?.name}</Typography>
                        <Typography>{formatDuration(searchResult.duration_ms)}</Typography>
                    </Box>
                    <Box>
                        <Typography variant="caption">Album: {searchResult.album.name}</Typography>
                    </Box>
                </CardContent>
            </CardActionArea>
        </Card>
    )
}