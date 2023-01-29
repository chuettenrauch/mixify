import {Container, Fab, Stack} from "@mui/material";
import {useState} from "react";
import MixtapeCard from "../components/MixtapeCard";
import {Add as AddIcon} from "@mui/icons-material";
import MixtapeForm from "../components/MixtapeForm";
import useMixtapes from "../hooks/useMixtapes";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";

export default function MixtapesOverviewPage() {
    const [mixtapes, setMixtapes] = useMixtapes();
    const [isMixtapeFormOpen, setIsMixtapeFormOpen] = useState(false);

    const openMixtapeForm = () => {
        setIsMixtapeFormOpen(true);
    }

    const closeMixtapeForm= () => {
        setIsMixtapeFormOpen(false);
    }

    const addMixtape = (savedMixtape: Mixtape) => {
        setMixtapes([...mixtapes, savedMixtape]);
        closeMixtapeForm();
    }

    return (
        <Container sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
            p: 0,
        }}>
            <PageHeader title="Your Mixtapes"/>

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

            <MixtapeForm title="Create mixtape" open={isMixtapeFormOpen} onSave={addMixtape} onClose={closeMixtapeForm}/>
        </Container>
    );
}