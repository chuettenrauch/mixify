import {Box, IconButton, Typography} from "@mui/material";
import {PlayCircle as PlayCircleIcon} from "@mui/icons-material";
import FlippableImageCard from "./FlippableImageCard";
import React from "react";
import UserAvatar from "./UserAvatar";
import MixtapeUtils from "../utils/mixtape-utils";
import Mixtape from "../types/mixtape";

export default function PlayerMixtapeView({mixtape, ready, onPlay}: {
    mixtape: Mixtape,
    ready: boolean,
    onPlay: () => void,
}) {
    return (
        <>
            <FlippableImageCard image={{src: mixtape.imageUrl, alt: mixtape.title}}
                                textOnBack={mixtape.description}>
                {ready &&
                  <IconButton size="large" component="button" onClick={() => onPlay()} aria-label="play"
                              sx={{
                                  display: "block",
                                  m: 4,
                                  position: "absolute",
                                  top: 0,
                                  left: 0,
                                  width: "80%",
                                  height: "80%",
                                  zIndex: 1,
                              }}>
                    <PlayCircleIcon sx={{width: "70%", height: "100%"}}/>
                  </IconButton>
                }
            </FlippableImageCard>

            <Box sx={{display: "flex", gap: 1, width: "100%"}}>
                <UserAvatar user={mixtape.createdBy} sx={{width: 60, height: 60}}/>
                <Box sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography variant="h2" textTransform="uppercase">by {mixtape.createdBy.name}</Typography>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                </Box>
            </Box>

        </>
    )
}