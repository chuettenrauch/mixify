import {Box, Fab, IconButton, Stack, TextField, Typography} from "@mui/material";
import MixtapeCard from "../components/MixtapeCard";
import {Add as AddIcon, Clear as ClearIcon} from "@mui/icons-material";
import MixtapeForm from "../components/MixtapeForm";
import useMixtapes from "../hooks/useMixtapes";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import {useNavigate} from "react-router-dom";
import useForm from "../hooks/useForm";
import React, {useState} from "react";
import MessageContainer from "../components/MessageContainer";
import MixtapeUtils from "../utils/mixtape-utils";

export default function MixtapesOverviewPage() {
    const navigate = useNavigate();
    const [mixtapes, setMixtapes] = useMixtapes();
    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();
    const [searchTerm, setSearchTerm] = useState<string>("");

    const navigateToMixtapeDetailPage = (mixtape: Mixtape) => {
        navigate(`/mixtapes/${mixtape.id}`);
    }

    const removeMixtapeFromList = (deletedMixtape: Mixtape) => {
        setMixtapes(mixtapes.filter(mixtape => mixtape.id !== deletedMixtape.id));
    }

    return (
        <Box sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
        }}>
            <PageHeader title="Your Mixtapes"/>

            <TextField
                variant="standard"
                id="search"
                name="search"
                value={searchTerm}
                placeholder="Search mixtapes..."
                onChange={(e) => setSearchTerm(e.target.value)}
                sx={{width: "100%"}}
                InputProps={{
                    endAdornment: (
                        <>
                            <IconButton
                                sx={{ visibility: searchTerm ? "visible" : "hidden" }}
                                onClick={() => setSearchTerm("")}
                            >
                                <ClearIcon/>
                            </IconButton>
                        </>
                    ),
                }}
            />

            <Stack spacing={2} sx={{width: "100%"}}>
                {mixtapes.length === 0
                    ? <MessageContainer>
                        <Typography>You don't have any mixtapes, yet.</Typography>
                    </MessageContainer>
                    : mixtapes
                        .filter(mixtape => MixtapeUtils.containsSearchTerm(mixtape, searchTerm))
                        .map(mixtape => (
                            <MixtapeCard key={mixtape.id} mixtape={mixtape} onEdit={navigateToMixtapeDetailPage}
                                         onDelete={removeMixtapeFromList}/>
                        ))}
            </Stack>

            <Fab color="primary" size="medium" onClick={openMixtapeForm} aria-label="add" sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(9),
                right: (theme) => theme.spacing(2)}
            }>
                <AddIcon/>
            </Fab>

            {isMixtapeFormOpen && <MixtapeForm title="Create mixtape" open={isMixtapeFormOpen} onSave={navigateToMixtapeDetailPage} onClose={closeMixtapeForm}/>}
        </Box>
    );
}