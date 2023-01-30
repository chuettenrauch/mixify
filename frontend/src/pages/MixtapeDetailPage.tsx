import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {MixtapeApi} from "../api/mixify-api";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import MixtapeDetails from "../components/MixtapeDetails";
import {Container, Divider, Fab, Typography} from "@mui/material";
import {Add as AddIcon, Info as InfoIcon} from "@mui/icons-material";

export default function MixtapeDetailPage() {
    const navigate = useNavigate();
    const {id} = useParams<{id: string}>();
    const [mixtape, setMixtape] = useState<Mixtape|null>()

    useEffect(() => {
        if (!id) {
            return;
        }

        (async () => {
            const mixtape: Mixtape = await MixtapeApi.getMixtape(id);

            setMixtape(mixtape);
        })();
    }, [id]);

    const navigateToMixtapesOverviewPage = () => {
        navigate("/mixtapes");
    }

    if (!mixtape) {
        return null;
    }

    return (
        <Container sx={{
            display: "flex",
            flexDirection: "column",
            gap: 3,
            p: 0,
        }}>
            <PageHeader title={mixtape.title}/>

            <MixtapeDetails mixtape={mixtape} onEdit={(savedMixtape: Mixtape) => setMixtape(savedMixtape)} onDelete={navigateToMixtapesOverviewPage}/>
            <Divider sx={{width: "100%"}}/>

            <Typography variant="h2" component="h2" textTransform={"uppercase"}>Tracks</Typography>
            <Container sx={{display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center", minHeight: 200}}>
                {mixtape.tracks.length === 0
                    ? <>
                        <Typography>Your mixtape has no tracks yet.</Typography>
                        <Typography sx={{display: "flex", alignItems: "center"}}><InfoIcon color="primary"/> You can add up to 12 tracks.</Typography>
                    </>
                    : <></>
                }
            </Container>

            <Fab color="primary" size="medium" aria-label="add track" sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(9),
                right: (theme) => theme.spacing(2)}
            }>
                <AddIcon/>
            </Fab>

        </Container>
    );
}