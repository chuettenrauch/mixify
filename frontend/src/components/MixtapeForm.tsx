import {Container, IconButton, Modal, Typography} from "@mui/material";
import {Close as CloseIcon} from "@mui/icons-material";
import React from "react";

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
                paddingBlock: 9,
                width: "100%",
                height: "100vh",
                bgcolor: 'background.paper',
                position: "relative"
            }}>
                <IconButton
                    onClick={onClose}
                    sx={{
                        position: "absolute",
                        top: (theme) => theme.spacing(8),
                        right: (theme) => theme.spacing(1)
                    }}>
                    <CloseIcon/>
                </IconButton>
                <Typography variant="h1" component="h1" textTransform={"uppercase"}>Create Mixtape</Typography>
            </Container>
        </Modal>
    );
}