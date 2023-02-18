import {Box, Button, Container, Link, Paper, Typography} from "@mui/material";
import logo from "../logo.png";
import React from "react";
import {useSearchParams} from "react-router-dom";
import {toast} from "react-toastify";
import Utils from "../utils/utils";
import {Info as InfoIcon} from "@mui/icons-material";

const showDevModeInfo: boolean = !!Number(process.env.REACT_APP_SHOW_DEV_MODE_INFO);

export default function LoginPage() {
    const [query] = useSearchParams();

    const authorizationUrl = Utils.createOAuth2AuthorizationLink(query);

    if (query.has("error")) {
        const errorCode = query.get("error_code");
        const errorMessage = errorCode && errorCode === "invalid_user_info_response"
            ? "Mixify is currently in development mode. Your spotify account must be explicitly permitted to use this app."
            : query.get("error");

        toast.error(errorMessage, {
            position: "top-center",
            autoClose: false,
            hideProgressBar: false,
            closeOnClick: true,
            draggable: false,
            theme: "colored",
        });
    }

    return (
        <Container maxWidth="sm" sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            gap: 2,
            p: 3,
            textAlign: "center",
            height: window.innerHeight,
        }}>
                <img src={logo} alt="mixify logo"/>
                <Typography>
                    Bring back the nostalgia of creating and sharing mixtapes like in the good old days of cassette tapes
                </Typography>
                <Button variant="contained" href={authorizationUrl}>
                    Continue with spotify
                </Button>

            {showDevModeInfo &&
              <Paper sx={{
                  position: 'fixed',
                  bottom: 0,
                  left: 0,
                  width: "100%",
                  p: 2,
                  zIndex: (theme) => theme.zIndex.appBar - 1
              }} elevation={10}>
                <Box sx={{display: "flex", flexDirection: "column", alignItems: "center", gap: 1}}>
                  <Typography sx={{display: "flex", alignItems: "center"}}>
                    <InfoIcon color="primary"/>
                    Mixify is currently in development mode.
                  </Typography>
                  <Typography>Spotify premium account & explicit authorization through the developer required.
                    Request access <Link href="mailto:support@mixify.me">here</Link>.</Typography>
                </Box>
              </Paper>
            }
        </Container>
    );
}