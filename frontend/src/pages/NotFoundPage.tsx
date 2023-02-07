import {Box, Container, Typography} from "@mui/material";
import MessageContainer from "../components/MessageContainer";
import React from "react";
import {Link} from "react-router-dom";
import {HeadsetOff as HeadsetOffIcon} from "@mui/icons-material";

export default function NotFoundPage() {
    return (
        <Container sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            gap: 3,
            p: 0,
        }}>
            <MessageContainer minHeight={500}>
                <Box sx={{fontSize: 100, p: 0}}>
                    <HeadsetOffIcon color="primary" fontSize="inherit"/>
                </Box>
                <Typography variant="h1">404</Typography>
                <Typography>Sorry, we could not find any music here.</Typography>
                <Typography>Maybe start creating a new <Link to="/mixtapes">Mixtape</Link>?</Typography>
            </MessageContainer>

        </Container>
    )
}