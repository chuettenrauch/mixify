import {Container, IconButton, Modal, Stack, TextField, Typography} from "@mui/material";
import FormHeader from "./FormHeader";
import React, {FormEvent} from "react";
import {Clear as ClearIcon, MusicNote as MusicNoteIcon, Search as SearchIcon} from "@mui/icons-material";
import SearchResultCard from "./SearchResultCard";
import AddTrackForm from "./AddTrackForm";
import useForm from "../hooks/useForm";
import useSpotifyTrackSearch from "../hooks/useSpotifyTrackSearch";

export default function SearchTrackForm({open, onClose}: {
    open: boolean,
    onClose: () => void,
}) {
    const {isFormOpen: isAddTrackFormOpen, openForm: openAddTrackForm, closeForm: closeAddTrackForm} = useForm();

    const {
        searchTerm,
        setSearchTerm,
        searchResults,
        selectedSearchResult,
        setSelectedSearchResult,
        searchTracks,
        resetSearch
    } = useSpotifyTrackSearch();

    const onSearchSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        searchTracks(searchTerm);
    }

    const onSelectSearchResult = (selectedSearchResult: Spotify.Track) => {
        setSelectedSearchResult(selectedSearchResult);
        openAddTrackForm();
    }

    return (
        <Modal
            open={open}
            aria-labelledby="modal-search-track-form"
            aria-describedby="Form to search a track"
            sx={{zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Container sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                paddingInline: 2,
                paddingBlock: {xs: 9, sm: 10},
                gap: 2,
                width: "100%",
                height: window.innerHeight,
                bgcolor: 'background.paper',
                position: "relative",
                overflow: "scroll"
            }}>
                <FormHeader title="Search track" onClose={onClose}/>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSearchSubmit}
                >
                    <TextField
                        variant="standard"
                        id="search"
                        name="search"
                        value={searchTerm}
                        placeholder="Search track..."
                        onChange={(e) => setSearchTerm(e.target.value)}
                        InputProps={{
                            endAdornment: (
                                <>
                                    <IconButton
                                        sx={{ visibility: searchTerm ? "visible" : "hidden" }}
                                        onClick={resetSearch}
                                    >
                                        <ClearIcon/>
                                    </IconButton>
                                    <IconButton type="submit">
                                        <SearchIcon/>
                                    </IconButton>
                                </>
                            ),
                        }}
                    />
                </Stack>

                {searchResults.length === 0
                    ? <Container sx={{
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "center",
                        alignItems: "center",
                        gap: 1,
                        minHeight: 300
                    }}>
                        <MusicNoteIcon color="primary" sx={{fontSize: 50}}/>
                        <Typography variant="h2" textTransform="uppercase">Search for a
                            track</Typography>
                        <Typography>Find the track you're looking for above.</Typography>
                    </Container>
                    : <Stack spacing={2} sx={{width: "100%"}}>
                        <Typography variant="h2" textTransform="uppercase">Results</Typography>
                        {searchResults.map(searchResult => (
                            <SearchResultCard key={searchResult.id} searchResult={searchResult} onClick={onSelectSearchResult}/>
                        ))}
                    </Stack>
                }

                {isAddTrackFormOpen && selectedSearchResult &&
                    <AddTrackForm
                        open={isAddTrackFormOpen}
                        onBack={closeAddTrackForm}
                        onClose={onClose}
                        selectedSpotifyTrack={selectedSearchResult}
                    />
                }

            </Container>
        </Modal>
    );
}