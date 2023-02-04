import {Container, List, ListItem, Typography} from "@mui/material";
import FlippableTrackCard from "./FlippableTrackCard";
import {Info as InfoIcon} from "@mui/icons-material";
import React from "react";
import Mixtape from "../types/mixtape";
import Track from "../types/track";

export default function PlayedTracksList({mixtape, playedTracks, onTrackPlay}: {
    mixtape: Mixtape,
    playedTracks: Track[],
    onTrackPlay: (trackIndex: number) => void,
}) {
    return (
        <>
            <List sx={{display: "flex", flexDirection: "column", gap: 2, p: 0, width: "100%"}}>
                {playedTracks.map((track, index) => (
                    <ListItem key={track.id} sx={{p: 0}}>
                        <Container
                            sx={{display: "flex", justifyContent: "space-between", alignItems: "center", p: 0}}
                        >
                            <Typography variant="h1" component="h3" sx={{mr: 2}}>{index + 1}</Typography>
                            <FlippableTrackCard
                                track={track}
                                mixtape={mixtape}
                                onImageClick={() => onTrackPlay(index)}
                            />
                        </Container>
                    </ListItem>
                ))}
            </List>

            {playedTracks.length !== mixtape.tracks.length &&
              <Typography sx={{display: "flex", alignItems: "center"}}>
                <InfoIcon color="primary" sx={{mr: 1}}/>
                  {playedTracks.length === 0
                      ? "Start listening to this mixtape to reveal the tracks."
                      : "Continue listening to reveal the remaining tracks."
                  }
              </Typography>
            }
        </>
    )
}