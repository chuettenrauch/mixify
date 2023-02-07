import {
    Card,
    CardActionArea,
    CardActions,
    CardContent,
    CardMedia, Container,
    Typography
} from "@mui/material";
import React, {useState} from "react";
import Track from "../types/track";
import ReactCardFlip from "react-card-flip";
import Mixtape from "../types/mixtape";
import TrackCardMenu from "./TrackCardMenu";
import CardImageWithPlayButton from "./CardImageWithPlayButton";

export default function FlippableTrackCard({track, mixtape, onImageClick, onEdit, onDelete}: {
    track: Track,
    mixtape: Mixtape,
    onImageClick?: () => void,
    onEdit?: (savedTrack: Track) => void,
    onDelete?: (deletedTrack: Track) => void,
}) {
    const [isFlipped, setIsFlipped] = useState<boolean>(false);

    return (
        <ReactCardFlip isFlipped={isFlipped} flipSpeedBackToFront={1} flipSpeedFrontToBack={1} containerStyle={{width: "100%"}}>
            <Card elevation={5} sx={{display: "flex", position: "relative", p: 0}}>
                <Container sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}}>
                    <CardActions sx={{p: 0}}>
                        <CardImageWithPlayButton
                            image={{src: track.imageUrl, alt: track.name, size: 100}}
                            opacity={0.8}
                            onClick={onImageClick}
                        />
                    </CardActions>
                    <CardActionArea sx={{p: 0}} onClick={() => track.description ? setIsFlipped(true) : null}>
                        <CardContent sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                            <Typography variant="h3">{track.name}</Typography>
                            <Typography>{track.artist}</Typography>
                        </CardContent>
                    </CardActionArea>
                </Container>

                {onEdit && onDelete &&
                  <CardActions>
                    <TrackCardMenu track={track} mixtape={mixtape} onEdit={onEdit} onDelete={onDelete}/>
                  </CardActions>
                }
            </Card>

            <Card elevation={5} sx={{}}>
                <CardActionArea sx={{p: 2}} onClick={() => setIsFlipped(false)}>
                    <CardMedia
                        component="img"
                        image={track.imageUrl}
                        alt={track.name}
                        sx={{float: "left", mr: 2, width: 100, height: 100, lineHeight: 0, border: "1px solid grey", opacity: 0.4}}
                    />

                    <CardContent sx={{p: 0}}>
                        <Typography textAlign="justify" >{track.description}</Typography>
                    </CardContent>
                </CardActionArea>
            </Card>
        </ReactCardFlip>
    )
}