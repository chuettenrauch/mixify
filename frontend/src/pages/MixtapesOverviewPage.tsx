import {Container, Stack, Typography} from "@mui/material";
import Mixtape from "../types/Mixtape";
import {useEffect, useState} from "react";
import {MixtapeApi} from "../api/mixify-api";
import MixtapeCard from "../components/MixtapeCard";

export default function MixtapesOverviewPage() {
    const [mixtapes, setMixtapes] = useState<Mixtape[]>([]);

    useEffect(() => {
        (async () => {
            const mixtapes = await MixtapeApi.getMixtapes();
            setMixtapes(mixtapes);
        })();
    }, []);

    return (
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
    );
}