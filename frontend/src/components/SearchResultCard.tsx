import {
    Card,
    CardActionArea,
    CardContent,
    CardMedia,
    Typography
} from "@mui/material";
import React from "react";

export default function SearchResultCard({searchResult}: {
    searchResult: Spotify.Track,
}) {
    return (
        <Card elevation={5} sx={{display: "flex"}}>
            <CardActionArea sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}}>
                <CardMedia
                    component="img"
                    image={searchResult.album.images[0]?.url}
                    alt={searchResult.name}
                    sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey"}}
                />

                <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography variant="h3">{searchResult.name}</Typography>
                    <Typography>{searchResult.artists[0]?.name}</Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    )
}