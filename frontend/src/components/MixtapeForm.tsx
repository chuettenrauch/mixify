import {Container, Modal} from "@mui/material";
import React from "react";
import FormHeader from "./FormHeader";

export default function MixtapeForm({open, onClose}: {
    open: boolean
    onClose: () => void,
}) {
    return (
        <Modal
            open={open}
            aria-labelledby="modal-mixtape-form"
            aria-describedby="Form to create a new mixtape"
            sx={{overflow: "scroll", zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Container sx={{
                paddingInline: 2,
                paddingBlock: 10,
                width: "100%",
                height: "100vh",
                bgcolor: 'background.paper',
                position: "relative"
            }}>
                <FormHeader title="Create Mixtape" onClose={onClose}/>
            </Container>
        </Modal>
    );
}