import {Card, CardActionArea, CardContent, CardMedia, Typography} from "@mui/material";
import React, {useState} from "react";
import Track from "../types/track";
import ReactCardFlip from "react-card-flip";

export default function FlippableTrackCard({track}: {
    track: Track,
}) {
    const [isFlipped, setIsFlipped] = useState<boolean>(false);

    return (
        <ReactCardFlip isFlipped={isFlipped} flipSpeedBackToFront={1} flipSpeedFrontToBack={1} containerStyle={{width: "100%"}}>
            <Card elevation={5} sx={{display: "flex", flexDirection: "column", flex: 1}}>
                <CardActionArea sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}} onClick={() => setIsFlipped(true)}>
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey"}}
                    />

                    <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                        <Typography variant="h3">{track.name}</Typography>
                        <Typography>{track.artist}</Typography>
                    </CardContent>
                </CardActionArea>
            </Card>

            <Card elevation={5} sx={{}}>
                <CardActionArea sx={{p: 2}} onClick={() => setIsFlipped(false)}>
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{float: "left", mr: 2, width: 100, height: 100, lineHeight: 0, border: "1px solid grey", opacity: 0.6}}
                    />

                    <CardContent sx={{p: 0}}>
                        <Typography textAlign="justify" >{track.description}</Typography>
                    </CardContent>
                </CardActionArea>
            </Card>
        </ReactCardFlip>
    )
}