import {Box, Fab, IconButton, Stack, TextField, Typography} from "@mui/material";
import MixtapeCard from "../components/MixtapeCard";
import {Add as AddIcon, Clear as ClearIcon, FilterList as FilterListIcon} from "@mui/icons-material";
import MixtapeForm from "../components/MixtapeForm";
import useMixtapes from "../hooks/useMixtapes";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import {useNavigate} from "react-router-dom";
import useForm from "../hooks/useForm";
import React, {useEffect, useState} from "react";
import MessageContainer from "../components/MessageContainer";
import MixtapeUtils from "../utils/mixtape-utils";
import FilterModal from "../components/FilterModal";
import Filter from "../types/filter";
import {useAuthenticatedUser} from "../components/ProtectedRoutes";
import useOpenClose from "../hooks/useOpenClose";

export default function MixtapesOverviewPage() {
    const navigate = useNavigate();
    const {user} = useAuthenticatedUser();

    const [mixtapes, setMixtapes] = useMixtapes();
    const [filterResult, setFilterResult] = useState<Mixtape[]>([]);

    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();
    const {isOpen: isFilterModalOpen, open: openFilterModal, close: closeFilterModal} = useOpenClose();

    const [searchTerm, setSearchTerm] = useState<string>("");

    const navigateToMixtapeDetailPage = (mixtape: Mixtape) => {
        navigate(`/mixtapes/${mixtape.id}`);
    }

    const removeMixtapeFromList = (deletedMixtape: Mixtape) => {
        setMixtapes(mixtapes.filter(mixtape => mixtape.id !== deletedMixtape.id));
    }

    const onFilter = (filters: Filter[]) => {
        let filtered = mixtapes;

        for (const filter of filters) {
            filtered = filtered.filter(mixtape => filter(mixtape, user));
        }

        setFilterResult(filtered);
    }

    useEffect(() => {
        setFilterResult([...mixtapes]);
    }, [mixtapes, setFilterResult])

    const searchResult = filterResult.filter(mixtape => MixtapeUtils.containsSearchTerm(mixtape, searchTerm));

    return (
        <Box sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
        }}>
            <PageHeader title="Your Mixtapes"/>

            <Box sx={{display: "flex", width: "100%"}}>
                <TextField
                    variant="standard"
                    id="search"
                    name="search"
                    value={searchTerm}
                    placeholder="Search mixtapes..."
                    onChange={(e) => setSearchTerm(e.target.value)}
                    sx={{flex: 1}}
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
                <IconButton onClick={openFilterModal}>
                    <FilterListIcon/>
                </IconButton>
            </Box>

            <Stack spacing={2} sx={{width: "100%"}}>
                {mixtapes.length === 0 || searchResult.length === 0
                    ? <MessageContainer>
                        <Typography>No mixtapes found.</Typography>
                    </MessageContainer>
                    : <>
                        {searchResult.map(mixtape => (
                            <MixtapeCard
                                key={mixtape.id}
                                mixtape={mixtape}
                                onEdit={navigateToMixtapeDetailPage}
                                onDelete={removeMixtapeFromList}
                            />
                        ))}
                    </>
                }
            </Stack>

            <Fab color="primary" size="medium" onClick={openMixtapeForm} aria-label="add" sx={{
                position: "fixed",
                bottom: (theme) => theme.spacing(9),
                right: (theme) => theme.spacing(2)}
            }>
                <AddIcon/>
            </Fab>

            {isMixtapeFormOpen && <MixtapeForm title="Create mixtape" open={isMixtapeFormOpen} onSave={navigateToMixtapeDetailPage} onClose={closeMixtapeForm}/>}
            <FilterModal open={isFilterModalOpen} onFilter={onFilter} onClear={() => setFilterResult(mixtapes)} onClose={closeFilterModal}/>
        </Box>
    );
}