import {Container, Modal, Typography} from "@mui/material";
import FormHeader from "./FormHeader";
import React from "react";

export default function AddTrackForm({selectedSpotifyTrack, open, onBack, onClose}: {
    selectedSpotifyTrack: Spotify.Track,
    open: boolean,
    onBack: () => void,
    onClose: () => void,
}) {
    return (
        <Modal
            open={open}
            aria-labelledby="modal-search-track-form"
            aria-describedby="Form to search a track"
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
                <FormHeader title="Add track" onBack={onBack} onClose={onClose}/>

                <Typography>{selectedSpotifyTrack.name}</Typography>
            </Container>
        </Modal>
    );
}