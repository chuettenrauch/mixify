import {Box, Card, CardActionArea, CardActions, CardContent, CardMedia, Typography} from "@mui/material";
import PlayerProgressBar from "./PlayerProgressBar";
import React from "react";
import Track from "../types/track";
import PlayerControls from "./PlayerControls";

export default function MiniPlayer({track, onClick}: {
    track: Track,
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
                    <PlayerControls size={40}/>
                </CardActions>
            </Box>
            <PlayerProgressBar showDuration={false}/>
        </Card>
    )
}