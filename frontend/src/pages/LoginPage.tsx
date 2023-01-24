import {Button, Container, Typography} from "@mui/material";

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
                <Button variant="contained" href="/oauth/authorize/spotify">
                    Continue with spotify
                </Button>
        </Container>
    );
}