import {Button, Container, Modal, Stack, TextField} from "@mui/material";
import React, {ChangeEvent, FormEvent, useState} from "react";
import FormHeader from "./FormHeader";
import Form from "../types/forms";
import Mixtape from "../types/mixtape";
import {Save as SaveIcon} from "@mui/icons-material";

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

    const onSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log(mixtapeForm);
    };

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
            sx={{overflow: "scroll", zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Container sx={{
                display: "flex", flexDirection: "column", alignItems: "center",
                paddingInline: 2,
                paddingBlock: {xs: 9, sm: 10},
                width: "100%",
                height: "100vh",
                bgcolor: 'background.paper',
                position: "relative"
            }}>
                <FormHeader title="Create Mixtape" onClose={onClose}/>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <input
                        required
                        hidden
                        readOnly
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