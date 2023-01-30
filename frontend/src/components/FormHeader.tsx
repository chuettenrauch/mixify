import {Container, IconButton, Typography} from "@mui/material";
import {Close as CloseIcon} from "@mui/icons-material";
import React from "react";

export default function FormHeader({title, onClose}: {
    title: string,
    onClose: () => void,
}) {
    return (
        <Container sx={{display: "flex", justifyContent: "center", p: 0, position: "relative"}}>
            <IconButton sx={{position: "absolute", top: 0, right: 0}} onClick={() => onClose()}>
                <CloseIcon/>
            </IconButton>
            <Typography variant="h1" component="h1" textTransform={"uppercase"}>{title}</Typography>
        </Container>
    )
}