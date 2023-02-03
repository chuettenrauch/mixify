import {Box, Container, IconButton, Modal, Typography} from "@mui/material";
import {PauseCircle as PauseCircleIcon, SkipPrevious as SkipPreviousIcon} from "@mui/icons-material";
import FlippableImageCard from "./FlippableImageCard";
import React from "react";
import Track from "../types/track";
import ModalHeader from "./ModalHeader";
import PlayerProgressBar from "./PlayerProgressBar";
import Mixtape from "../types/mixtape";

export default function PlayerTrackView({open, mixtape, track, ready, onPause, onPrevious, onClose}: {
    open: boolean,
    mixtape: Mixtape,
    track: Track,
    ready: boolean,
    onPause: () => void,
    onPrevious: () => void,
    onClose: () => void,
}) {
    return (
        <Modal
            open={open}
            aria-labelledby="modal-mixtape-form"
            aria-describedby="Form to create a new mixtape"
            sx={{zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Container sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                paddingInline: 2,
                paddingBlock: {xs: 9, sm: 10},
                gap: 2,
                width: "100%",
                height: window.innerHeight,
                bgcolor: 'background.paper',
                position: "relative",
                overflow: "scroll"
            }}>
                <ModalHeader title={mixtape.title} onBack={onClose}/>

                <FlippableImageCard image={{src: track.imageUrl, alt: track.name}} textOnBack={track.description}/>

                <Box sx={{width: "100%"}}>
                    <Box sx={{textAlign: "left", width: "100%", mb: 1}}>
                        <Typography variant="h2" textTransform="uppercase">{track.name}</Typography>
                        <Typography>{track.artist}</Typography>
                    </Box>

                    <PlayerProgressBar/>

                    {ready &&
                      <Box sx={{display: "flex", justifyContent: "center"}}>
                        <IconButton color="primary" sx={{fontSize: 100, p: 0}} onClick={onPrevious}>
                          <SkipPreviousIcon fontSize="inherit"/>
                        </IconButton>
                        <IconButton color="primary" sx={{fontSize: 100, p: 0}} onClick={onPause}>
                          <PauseCircleIcon fontSize="inherit"/>
                        </IconButton>
                      </Box>
                    }
                </Box>
            </Container>
        </Modal>
    )
}