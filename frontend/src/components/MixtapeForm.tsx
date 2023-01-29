import {Button, Container, IconButton, Modal, Skeleton, Stack, TextField} from "@mui/material";
import React, {ChangeEvent, FormEvent, useState} from "react";
import FormHeader from "./FormHeader";
import Form from "../types/forms";
import Mixtape from "../types/mixtape";
import {PhotoCamera as PhotoCameraIcon, Save as SaveIcon} from "@mui/icons-material";
import {FileApi, MixtapeApi} from "../api/mixify-api";
import FileMetadata from "../types/file-metadata";

const initialMixtapeData = {
    title: "",
    description: "",
    image: "",
}

export default function MixtapeForm({mixtape, open, onClose}: {
    mixtape?: Mixtape,
    open: boolean,
    onClose: () => void,
}) {
    const [mixtapeForm, setMixtapeForm] = useState<Form.Mixtape>(mixtape ?? initialMixtapeData);

    const [imagePreview, setImagePreview] = useState<string | null>(null);

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        await MixtapeApi.createMixtape(mixtapeForm);
        onClose();
    };

    const onImageChange = async (e: ChangeEvent<HTMLInputElement>) => {
        const {files} = e.target;
        if (!files || files.length === 0) {
            setImagePreview(null);
            return;
        }

        const fileToUpload: File = files[0];

        const reader = new FileReader();
        reader.addEventListener("load", () => {
            setImagePreview(reader.result as string);
        }, false);

        reader.readAsDataURL(fileToUpload);

        const metadata: FileMetadata = await FileApi.uploadFile(fileToUpload);

        setMixtapeForm({
            ...mixtapeForm,
            image: metadata.id
        });
    }

    const onChange = (e: ChangeEvent<HTMLInputElement>) => {
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
                <FormHeader title="Create Mixtape" onClose={onClose}/>

                <Stack
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                >
                    <Container sx={{height: 0, overflow: "hidden", paddingTop: "100%", position: "relative"}}>
                        {imagePreview
                            ? <img src={imagePreview} alt="preview" style={{
                                position: "absolute",
                                top: 0,
                                left: 0,
                                objectFit: "cover",
                                width: "100%",
                                height: "100%"}}
                            />
                            : <Skeleton variant="rectangular" animation={false} width="100%" height="100%" sx={{
                                position: "absolute",
                                top: 0,
                                left: 0,
                            }}/>

                        }
                    </Container>

                    <IconButton aria-label="upload image" component="label" sx={{zIndex: 1}}>
                        <input hidden accept="image/*" type="file" onChange={onImageChange}/>
                        <PhotoCameraIcon/>
                    </IconButton>
                </Stack>

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
                        onChange={onChange}
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
                        onChange={onChange}
                    />
                    <Button type="submit" variant="contained" startIcon={<SaveIcon/>}>
                        Save
                    </Button>
                </Stack>
            </Container>
        </Modal>
    );
}