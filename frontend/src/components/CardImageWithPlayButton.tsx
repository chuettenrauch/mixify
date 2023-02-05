import {Box, CardMedia, IconButton} from "@mui/material";
import React from "react";
import {PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import Image from "../types/image";
import {useGlobalConfig} from "../context/globalConfigContext";

export default function CardImageWithPlayButton({image, opacity = 1, onClick}: {
    image: Image,
    opacity?: number,
    onClick?: () => void,
}) {
    const globalConfig = useGlobalConfig();

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
                    border: "1px solid grey",
                    opacity: opacity,
                }}
            />
            {onClick && globalConfig.canUsePlayer &&
              <IconButton size="large" onClick={() => onClick()} aria-label="play" sx={{
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
            }
        </Box>
    )
}