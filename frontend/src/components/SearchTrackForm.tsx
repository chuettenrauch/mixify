import {Container, IconButton, Modal, Stack, TextField, Typography} from "@mui/material";
import FormHeader from "./FormHeader";
import React, {FormEvent, useState} from "react";
import {Clear as ClearIcon, MusicNote as MusicNoteIcon, Search as SearchIcon} from "@mui/icons-material";
import useSpotifyApi from "../hooks/useSpotifyApi";

export default function SearchTrackForm({open, onClose}: {
    open: boolean,
    onClose: () => void,
}) {
    const spotifyApi = useSpotifyApi();
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [searchResults, setSearchResults] = useState<Spotify.Track[]>([]);

    const searchTracks = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const results: Spotify.Track[] = await spotifyApi.searchTracks(searchTerm);
        setSearchResults(results);
    }

    const clearSearchTerm = () => {
        setSearchTerm("");
        setSearchResults([]);
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
                <FormHeader title="Add track" onClose={onClose}/>

                <Stack
                    component="form"
                    spacing={2}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={searchTracks}
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
                                        onClick={clearSearchTerm}
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
                    : <Container sx={{p: 0}}>
                        <Typography variant="h2" textTransform="uppercase">Results</Typography>
                        {searchResults.map(searchResult => (
                            <p key={searchResult.id}>{searchResult.name}</p>
                        ))}
                    </Container>
                }

            </Container>
        </Modal>
    );
}