import {Box, CardMedia, IconButton} from "@mui/material";
import React from "react";
import {PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import {Link} from "react-router-dom";
import Image from "../types/image";

export default function CardImage({image, link}: {
    image: Image,
    link: string,
}) {
    return (
        <Box sx={{position: "relative"}}>
            <CardMedia
                component="img"
                image={image.src}
                alt={image.alt}
                sx={{
                    width: image.size,
                    height: image.size,
                    lineHeight: 0,
                    border: "1px solid grey"
                }}
            />
            <IconButton size="large" component={Link} to={link} aria-label="play" sx={{
                display: "block",
                m: 0,
                position: "absolute",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                zIndex: 1
            }}>
                <PlayCircleIcon sx={{width: "70%", height: "100%"}}/>
            </IconButton>
        </Box>
    )
}