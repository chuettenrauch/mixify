import {Container, Fab, Stack, Typography} from "@mui/material";
import Mixtape from "../types/mixtape";
import {useEffect, useState} from "react";
import {MixtapeApi} from "../api/mixify-api";
import MixtapeCard from "../components/MixtapeCard";
import {Add as AddIcon} from "@mui/icons-material";
import MixtapeForm from "../components/MixtapeForm";

export default function MixtapesOverviewPage() {
    const [mixtapes, setMixtapes] = useState<Mixtape[]>([]);
    const [isMixtapeFormOpen, setIsMixtapeFormOpen] = useState(false);

    useEffect(() => {
        (async () => {
            const mixtapes = await MixtapeApi.getMixtapes();
            setMixtapes(mixtapes);
        })();
    }, []);

    const openMixtapeForm = () => {
        setIsMixtapeFormOpen(true);
    }

    const closeMixtapeForm= () => {
        setIsMixtapeFormOpen(false);
    }

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

            <Fab color="primary" size="medium" onClick={openMixtapeForm} aria-label="add" sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(9),
                right: (theme) => theme.spacing(2)}
            }>
                <AddIcon/>
            </Fab>

            <MixtapeForm open={isMixtapeFormOpen} onClose={closeMixtapeForm}/>
        </Container>
    );
}