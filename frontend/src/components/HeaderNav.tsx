import {AppBar, Avatar, Box, Container, IconButton, Toolbar, Typography} from "@mui/material";
import React from "react";
import {useUserContext} from "../context/userContext";

export default function HeaderNav() {
    const {user} = useUserContext();

    return (
        <AppBar position="static">
            <Container maxWidth="md">
                <Toolbar disableGutters>
                    <Typography sx={{ marginLeft: "auto" }} variant="h6" noWrap component="a" href="/">
                        Logo
                    </Typography>

                    <Box sx={{ marginLeft: "auto" }}>
                        <IconButton sx={{ p: 0 }}>
                            <Avatar alt={user?.name} src={user?.imageUrl}/>
                        </IconButton>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}