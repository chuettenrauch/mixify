import {Box, Card, CardActionArea, CardActions, CardContent, CardMedia, IconButton, Typography} from "@mui/material";
import {PauseCircle as PauseCircleIcon, SkipPrevious as SkipPreviousIcon} from "@mui/icons-material";
import PlayerProgressBar from "./PlayerProgressBar";
import React from "react";
import Track from "../types/track";

export default function MiniPlayer({track, onPause, onPrevious, onClick}: {
    track: Track,
    onPause: () => void,
    onPrevious: () => void,
    onClick: () => void,
}) {
    return (
        <Card
            square={true}
            variant="outlined"
            sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(7),
                left: 0,
                width: "100%",
                p: 0,
            }}
        >
            <Box sx={{display: "flex"}}>
                <CardActionArea onClick={onClick} sx={{
                    display: "flex",
                    justifyContent: "flex-start",
                    alignItems: "center",
                    width: "100%",
                    height: (theme) => theme.spacing(7)
                }}>
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{
                            width: (theme) => theme.spacing(7),
                            height: (theme) => theme.spacing(7),
                            objectFit: "cover",
                            lineHeight: 0,
                            border: "1px solid grey",
                        }}
                    />
                    <CardContent sx={{p: 0, flex: 1}}>
                        <Box sx={{p: 1}}>
                            <Typography variant="h3">{track.name}</Typography>
                            <Typography>{track.artist}</Typography>
                        </Box>
                    </CardContent>
                </CardActionArea>

                <CardActions>
                    <IconButton color="primary" sx={{fontSize: 40, p: 0}} onClick={onPrevious}>
                        <SkipPreviousIcon fontSize="inherit"/>
                    </IconButton>
                    <IconButton color="primary" sx={{fontSize: 40, p: 0}} onClick={onPause}>
                        <PauseCircleIcon fontSize="inherit"/>
                    </IconButton>
                </CardActions>
            </Box>
            <PlayerProgressBar showDuration={false}/>
        </Card>
    )
}