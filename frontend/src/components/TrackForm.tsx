import {Box, Button, Container, FormControl, FormHelperText, Modal, Stack, TextField, Typography} from "@mui/material";
import ModalHeader from "./ModalHeader";
import React, {ChangeEvent, FormEvent, useState} from "react";
import Form from "../types/forms";
import ImageUpload from "./ImageUpload";
import FileMetadata from "../types/file-metadata";
import Track from "../types/track";
import {toast} from "react-toastify";
import {Save as SaveIcon} from "@mui/icons-material";
import {TrackApi} from "../api/mixify-api";
import Mixtape from "../types/mixtape";
import {isAxiosError} from "axios";

const initialErrorState = {
    name: null,
    artist: null,
    imageUrl: null,
    providerUri: null,
    description: null,
}

export default function TrackForm({title, mixtape, selectedSpotifyTrack, track, open, onSave, onBack, onClose}: {
    title: string,
    mixtape: Mixtape,
    selectedSpotifyTrack?: Spotify.Track,
    track?: Track,
    open: boolean,
    onSave: (savedTrack: Track) => void,
    onBack?: () => void,
    onClose: () => void,
}) {
    const [trackForm, setTrackForm] = useState<Form.Track | Track>(getInitialState(selectedSpotifyTrack, track));
    const [errors, setErrors] = useState<Form.Track>(initialErrorState);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const savedTrack: Track = track
                ? await TrackApi.updateTrack(mixtape, trackForm as Track)
                : await TrackApi.createTrack(mixtape, trackForm);

            onSave(savedTrack);
            onClose();

            toast.success("Successfully saved track.");
        } catch (e) {
            if (isAxiosError(e) && e.response && e.response.status === 400) {
                setErrors(e.response.data);
            }
        }
    };

    const onImageUpload = (fileMetadata: FileMetadata) => {
        setTrackForm({...trackForm, imageUrl: fileMetadata.url});
    };

    const onInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;

        setTrackForm({
            ...trackForm,
            [name]: value
        });
    };

    return (
        <Modal
            open={open}
            aria-labelledby="modal-add-track-form"
            aria-describedby="Form to add information for track"
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
                <ModalHeader title={title} onBack={onBack} onClose={onClose}/>

                <Box sx={{alignSelf: "flex-start"}}>
                    <Typography variant="h2">{trackForm.name}</Typography>
                    <Typography>{trackForm.artist}</Typography>
                </Box>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <FormControl error={!!errors.imageUrl}>
                        <ImageUpload imageUrl={trackForm.imageUrl} onUpload={onImageUpload}/>
                        <FormHelperText>{errors.imageUrl}</FormHelperText>
                    </FormControl>

                    <TextField
                        multiline
                        rows={6}
                        id="description"
                        name="description"
                        value={trackForm.description ?? ""}
                        label="Description"
                        placeholder="Why did you choose this song..."
                        margin="normal"
                        error={!!errors.description}
                        helperText={errors.description}
                        onChange={onInputChange}
                    />

                    <input
                        required
                        readOnly
                        hidden
                        id="name"
                        name="name"
                        value={trackForm.name ?? ""}
                    />

                    <input
                        readOnly
                        hidden
                        id="artist"
                        name="artist"
                        value={trackForm.artist ?? ""}
                    />

                    <input
                        readOnly
                        hidden
                        id="imageUrl"
                        name="imageUrl"
                        value={trackForm.imageUrl ?? ""}
                    />

                    <input
                        readOnly
                        hidden
                        id="providerUri"
                        name="providerUri"
                        value={trackForm.providerUri ?? ""}
                    />

                    <Button type="submit" variant="contained" startIcon={<SaveIcon/>}>
                        Save
                    </Button>
                </Stack>
            </Container>
        </Modal>
    );
}

function getInitialState(selectedSpotifyTrack: Spotify.Track | undefined, track: Track | undefined) {
    if (selectedSpotifyTrack) {
        return {
            name: selectedSpotifyTrack.name,
            artist: selectedSpotifyTrack.artists.at(0)?.name ?? "",
            imageUrl: selectedSpotifyTrack.album.images.at(0)?.url ?? "",
            providerUri: selectedSpotifyTrack.uri,
            description: null,
        }
    }

    if (track) {
        return track;
    }

    return {
        name: null,
        artist: null,
        imageUrl: null,
        providerUri: null,
        description: null,
    }
}