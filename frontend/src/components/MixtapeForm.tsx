import {Button, Container, Modal, Stack, TextField} from "@mui/material";
import React, {ChangeEvent, FormEvent, useState} from "react";
import FormHeader from "./FormHeader";
import Form from "../types/forms";
import Mixtape from "../types/mixtape";
import {Save as SaveIcon} from "@mui/icons-material";
import {MixtapeApi} from "../api/mixify-api";
import FileMetadata from "../types/file-metadata";
import ImageUpload from "./ImageUpload";

const initialMixtapeData = {
    title: "",
    description: "",
    image: "",
}

export default function MixtapeForm({title, mixtape, open, onSave, onClose}: {
    title: string,
    mixtape?: Mixtape,
    open: boolean,
    onSave: (savedMixtape: Mixtape) => void,
    onClose: () => void,
}) {
    const [mixtapeForm, setMixtapeForm] = useState<Form.Mixtape>(mixtape ?? initialMixtapeData);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const mixtape: Mixtape = await MixtapeApi.createMixtape(mixtapeForm);
        onSave(mixtape)
    };

    const onImageUpload = (fileMetadata: FileMetadata) => {
        setMixtapeForm({
            ...mixtapeForm,
            image: fileMetadata.id
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
                width: "100%",
                height: "100vh",
                bgcolor: 'background.paper',
                position: "relative",
                overflow: "scroll"
            }}>
                <FormHeader title={title} onClose={onClose}/>

                <ImageUpload imageUrl={mixtape ? `/api/files/${mixtape.image}` : null} onUpload={onImageUpload}/>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <input
                        required
                        readOnly
                        hidden
                        id="image"
                        name="image"
                        value={mixtapeForm.image}
                    />

                    <TextField
                        required
                        variant="standard"
                        id="title"
                        name="title"
                        value={mixtapeForm.title}
                        label="Title"
                        placeholder="Mixtape title"
                        margin="normal"
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
                    <Button type="submit" variant="contained" startIcon={<SaveIcon/>}>
                        Save
                    </Button>
                </Stack>
            </Container>
        </Modal>
    );
}