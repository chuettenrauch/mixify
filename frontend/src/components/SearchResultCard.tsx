import {
    Box,
    Card,
    CardActionArea, CardActions,
    CardContent,
    CardMedia,
    Typography
} from "@mui/material";
import React from "react";
import formatDuration from "format-duration";
import AudioButton from "./AudioButton";

export default function SearchResultCard({searchResult}: {
    searchResult: Spotify.Track,
}) {
    return (
        <Card elevation={5} sx={{display: "flex", position: "relative"}}>
            <CardActionArea sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2, flex: 1}}>
                <CardMedia
                    component="img"
                    image={searchResult.album.images[0]?.url}
                    alt={searchResult.name}
                    sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey", alignSelf: "center"}}
                />

                <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "space-between", p: 0, pl: 2}}>
                    <Box>
                        <Typography variant="h3">{searchResult.name}</Typography>
                        <Typography>{searchResult.artists[0]?.name}</Typography>
                        <Typography variant="caption" sx={{overflow: "hidden"}}>{searchResult.album.name}</Typography>
                    </Box>
                    <Box>
                        <Typography>{formatDuration(searchResult.duration_ms)}</Typography>
                    </Box>
                </CardContent>
            </CardActionArea>
            <CardActions sx={{display: "flex", flexDirection: "column", p: 0, flex: 0}}>
                <AudioButton src={searchResult.preview_url}/>
            </CardActions>
        </Card>
    )
}

/* this is necessary as the official types of the spotify webplayback sdk do not include the preview_url */
declare global {
    namespace Spotify {
        interface Track {
            preview_url: string;
        }
    }
}