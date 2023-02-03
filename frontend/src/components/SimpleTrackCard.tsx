import {Card, CardContent, CardMedia, Container, Typography} from "@mui/material";
import React from "react";
import Track from "../types/track";

export default function SimpleTrackCard({track}: {
    track: Track,
}) {
    return (
        <Card elevation={5} sx={{display: "flex", flexDirection: "column", flex: 1}}>
            <Container sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}}>
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
            </Container>
        </Card>
    )
}