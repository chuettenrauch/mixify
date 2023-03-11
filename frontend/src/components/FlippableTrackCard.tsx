import {
    Card,
    CardActionArea,
    CardActions,
    CardContent,
    CardMedia, IconButton,
    Typography
} from "@mui/material";
import React, {useState} from "react";
import Track from "../types/track";
import ReactCardFlip from "react-card-flip";
import Mixtape from "../types/mixtape";
import TrackCardMenu from "./TrackCardMenu";
import {useGlobalConfig} from "../context/globalConfigContext";
import {PlayCircle as PlayCircleIcon} from "@mui/icons-material";

export default function FlippableTrackCard({track, mixtape, onEdit, onDelete, onPlay}: {
    track: Track,
    mixtape: Mixtape,
    onEdit?: (savedTrack: Track) => void,
    onDelete?: (deletedTrack: Track) => void,
    onPlay?: () => void,
}) {
    const globalConfig = useGlobalConfig();
    const [isFlipped, setIsFlipped] = useState<boolean>(false);

    return (
        <ReactCardFlip isFlipped={isFlipped} flipSpeedBackToFront={1} flipSpeedFrontToBack={1} containerStyle={{width: "100%"}}>
            <Card elevation={5} sx={{display: "flex", position: "relative"}}>
                <CardActionArea
                    onClick={() => track.description ? setIsFlipped(true) : null}
                    sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2, flex: 1}}
                >
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey", alignSelf: "center"}}
                    />
                    <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                        <Typography variant="h3">{track.name}</Typography>
                        <Typography>{track.artist}</Typography>
                    </CardContent>
                </CardActionArea>

                {onEdit && onDelete &&
                    <CardActions sx={{display: "flex", flexDirection: "column", p: 0, flex: 0}}>
                        <TrackCardMenu track={track} mixtape={mixtape} onEdit={onEdit} onDelete={onDelete}/>
                    </CardActions>
                }

                {onPlay && globalConfig.canUsePlayer &&
                    <CardActions sx={{display: "flex", flexDirection: "column", p: 0, flex: 0}}>
                        <IconButton onClick={() => onPlay()} aria-label="play" sx={{
                            alignSelf: "flex-start",
                            position: "absolute",
                            top: (theme) => theme.spacing(1),
                            right: 0,
                        }}>
                            <PlayCircleIcon/>
                        </IconButton>
                    </CardActions>
                }
            </Card>

            <Card elevation={5}>
                <CardActionArea sx={{p: 2}} onClick={() => setIsFlipped(false)}>
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{float: "left", mr: 2, width: 100, height: 100, lineHeight: 0, border: "1px solid grey"}}
                    />

                    <CardContent sx={{p: 0}}>
                        <Typography textAlign="justify" >{track.description}</Typography>
                    </CardContent>
                </CardActionArea>
            </Card>
        </ReactCardFlip>
    )
}