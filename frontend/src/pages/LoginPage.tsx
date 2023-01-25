import {Button, Container, Typography} from "@mui/material";

const authorizeUrl = `${process.env.REACT_APP_API_BASE_URL ?? ""}/oauth2/authorization/spotify`;

export default function LoginPage() {
    return (
        <Container maxWidth="md" sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            gap: 2,
            p: 3,
            textAlign: "center",
            height: "100vh"
        }}>
                <Typography variant="h2" component="h1">mixify</Typography>
                <Typography>
                    Bring back the nostalgia of creating and sharing mixtapes like in the good old days of cassette tapes
                </Typography>
                <Button variant="contained" href={authorizeUrl}>
                    Continue with spotify
                </Button>
        </Container>
    );
}