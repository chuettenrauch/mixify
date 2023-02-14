import {Button, Container, Typography} from "@mui/material";
import logo from "../logo.png";
import React from "react";
import {useSearchParams} from "react-router-dom";
import {toast} from "react-toastify";
import Utils from "../utils/utils";

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
            height: "100dvh",
        }}>
                <img src={logo} alt="mixify logo"/>
                <Typography>
                    Bring back the nostalgia of creating and sharing mixtapes like in the good old days of cassette tapes
                </Typography>
                <Button variant="contained" href={authorizationUrl}>
                    Continue with spotify
                </Button>
        </Container>
    );
}