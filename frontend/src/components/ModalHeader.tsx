import {Container, IconButton, Typography} from "@mui/material";
import {ChevronLeft as ChevronLeftIcon, Close as CloseIcon} from "@mui/icons-material";
import React from "react";

export default function ModalHeader({title, onBack, onClose}: {
    title: string,
    onBack?: () => void,
    onClose?: () => void,
}) {
    return (
        <Container sx={{display: "flex", justifyContent: "center", p: 0, position: "relative"}}>
            {onBack &&
              <IconButton sx={{position: "absolute", top: 0, left: 0}} onClick={onBack}>
                <ChevronLeftIcon/>
              </IconButton>
            }
            <Typography variant="h1" component="h1" textTransform={"uppercase"}>{title}</Typography>
            {onClose &&
              <IconButton sx={{position: "absolute", top: 0, right: 0}} onClick={() => onClose()}>
                <CloseIcon/>
              </IconButton>
            }
        </Container>
    )
}