import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {MixtapeApi} from "../api/mixify-api";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import MixtapeDetails from "../components/MixtapeDetails";
import { Container } from "@mui/material";

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
            alignItems: "center",
            gap: 3,
            p: 0,
        }}>
            <PageHeader title={mixtape.title}/>

            <MixtapeDetails mixtape={mixtape} onEdit={(savedMixtape: Mixtape) => setMixtape(savedMixtape)} onDelete={navigateToMixtapesOverviewPage}/>
        </Container>
    );
}