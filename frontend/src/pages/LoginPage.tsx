import {Button, Container, Typography} from "@mui/material";
import logo from "../logo.png";
import React from "react";
import {useSearchParams} from "react-router-dom";
import {toast} from "react-toastify";

const authorizeUrl = `${process.env.REACT_APP_API_BASE_URL ?? ""}/oauth2/authorization/spotify`;

export default function LoginPage() {
    const [query] = useSearchParams();

    if (query.has("error")) {
        const errorCode = query.get("errorCode");
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
        <Container maxWidth="md" sx={{
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
                <Button variant="contained" href={authorizeUrl}>
                    Continue with spotify
                </Button>
        </Container>
    );
}