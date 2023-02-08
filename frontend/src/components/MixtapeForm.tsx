import {Button, Container, FormControl, FormHelperText, Modal, Stack, TextField} from "@mui/material";
import React, {ChangeEvent, FormEvent, useState} from "react";
import ModalHeader from "./ModalHeader";
import Form from "../types/forms";
import Mixtape from "../types/mixtape";
import {Save as SaveIcon} from "@mui/icons-material";
import {MixtapeApi} from "../api/mixify-api";
import FileMetadata from "../types/file-metadata";
import ImageUpload from "./ImageUpload";
import {toast} from "react-toastify";
import {isAxiosError} from "axios";

const initialMixtapeData = {
    title: null,
    description: null,
    imageUrl: null,
}

const initialErrorData = initialMixtapeData;

export default function MixtapeForm({title, mixtape, open, onSave, onClose}: {
    title: string,
    mixtape?: Mixtape,
    open: boolean,
    onSave: (savedMixtape: Mixtape) => void,
    onClose: () => void,
}) {
    const [mixtapeForm, setMixtapeForm] = useState<Form.Mixtape | Mixtape>(mixtape ?? initialMixtapeData);
    const [errors, setErrors] = useState<Form.Mixtape>(initialErrorData);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const savedMixtape: Mixtape = mixtape
                ? await MixtapeApi.updateMixtape(mixtapeForm as Mixtape)
                : await MixtapeApi.createMixtape(mixtapeForm);

            onSave(savedMixtape);
            onClose();

            toast.success("Successfully saved mixtape.");
        } catch (e) {
            if (isAxiosError(e) && e.response && e.response.status === 400) {
                setErrors(e.response.data);
            }
        }
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

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <FormControl error={!!errors.imageUrl}>
                        <ImageUpload imageUrl={mixtape ? mixtape.imageUrl : null} onUpload={onImageUpload}/>
                        <FormHelperText>{errors.imageUrl}</FormHelperText>
                    </FormControl>

                    <TextField
                        variant="standard"
                        id="title"
                        name="title"
                        value={mixtapeForm.title}
                        label="Title"
                        placeholder="Mixtape title"
                        margin="none"
                        error={!!errors.title}
                        helperText={errors.title}
                        onChange={onInputChange}
                    />
                    <TextField
                        multiline
                        rows={6}
                        id="description"
                        name="description"
                        value={mixtapeForm.description}
                        label="Description"
                        placeholder="What is your mixtape about..."
                        margin="normal"
                        error={!!errors.description}
                        helperText={errors.description}
                        onChange={onInputChange}
                    />
                    <input
                        readOnly
                        hidden
                        id="imageUrl"
                        name="imageUrl"
                        value={mixtapeForm.imageUrl ?? ""}
                    />

                    <Button type="submit" variant="contained" startIcon={<SaveIcon/>}>
                        Save
                    </Button>
                </Stack>
            </Container>
        </Modal>
    );
}