import {Button, Container, Modal, Stack, TextField} from "@mui/material";
import React, {ChangeEvent, FormEvent, useState} from "react";
import ModalHeader from "./ModalHeader";
import Form from "../types/forms";
import Mixtape from "../types/mixtape";
import {Save as SaveIcon} from "@mui/icons-material";
import {MixtapeApi} from "../api/mixify-api";
import FileMetadata from "../types/file-metadata";
import ImageUpload from "./ImageUpload";
import {toast} from "react-toastify";

const initialMixtapeData = {
    title: "",
    description: "",
    imageUrl: "",
}

export default function MixtapeForm({title, mixtape, open, onSave, onClose}: {
    title: string,
    mixtape?: Mixtape,
    open: boolean,
    onSave: (savedMixtape: Mixtape) => void,
    onClose: () => void,
}) {
    const [mixtapeForm, setMixtapeForm] = useState<Form.Mixtape | Mixtape>(mixtape ?? initialMixtapeData);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const savedMixtape: Mixtape = mixtape
            ? await MixtapeApi.updateMixtape(mixtapeForm as Mixtape)
            : await MixtapeApi.createMixtape(mixtapeForm);

        onSave(savedMixtape);
        onClose();

        toast.success("Successfully saved mixtape.");
    };

    const onImageUpload = (fileMetadata: FileMetadata) => {
        setMixtapeForm({
            ...mixtapeForm,
            imageUrl: fileMetadata.url
        });
    }

    const onInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;

        setMixtapeForm({
            ...mixtapeForm,
            [name]: value
        });
    };

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
                <ModalHeader title={title} onClose={onClose}/>

                <ImageUpload imageUrl={mixtape ? mixtape.imageUrl : null} onUpload={onImageUpload}/>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <TextField
                        required
                        variant="standard"
                        id="title"
                        name="title"
                        value={mixtapeForm.title}
                        label="Title"
                        placeholder="Mixtape title"
                        onChange={onInputChange}
                    />
                    <TextField
                        required
                        multiline
                        rows={6}
                        id="description"
                        name="description"
                        value={mixtapeForm.description}
                        label="Description"
                        placeholder="What is your mixtape about..."
                        margin="normal"
                        onChange={onInputChange}
                    />
                    <input
                        required
                        readOnly
                        hidden
                        id="imageUrl"
                        name="imageUrl"
                        value={mixtapeForm.imageUrl}
                    />

                    <Button type="submit" variant="contained" startIcon={<SaveIcon/>}>
                        Save
                    </Button>
                </Stack>
            </Container>
        </Modal>
    );
}