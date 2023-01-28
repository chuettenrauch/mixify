import {Container, Fab, Stack, Typography} from "@mui/material";
import Mixtape from "../types/mixtape";
import {useEffect, useState} from "react";
import {MixtapeApi} from "../api/mixify-api";
import MixtapeCard from "../components/MixtapeCard";
import {Add as AddIcon} from "@mui/icons-material";

export default function MixtapesOverviewPage() {
    const [mixtapes, setMixtapes] = useState<Mixtape[]>([]);

    useEffect(() => {
        (async () => {
            const mixtapes = await MixtapeApi.getMixtapes();
            setMixtapes(mixtapes);
        })();
    }, []);

    return (
        <>
            <Container sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 3,
                p: 0,
            }}>
                <Typography variant="h1" component="h1" textTransform={"uppercase"}>Your Mixtapes</Typography>

                <Stack spacing={2} sx={{width: "100%"}}>
                    {mixtapes.map(mixtape => (
                        <MixtapeCard key={mixtape.id} mixtape={mixtape}/>
                    ))}
                </Stack>
            </Container>

            <Fab color="primary" size="medium" aria-label="add" sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(9),
                right: (theme) => theme.spacing(2)}
            }>
                <AddIcon/>
            </Fab>
        </>
    );
}