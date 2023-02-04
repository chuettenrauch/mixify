import {useNavigate, useParams} from "react-router-dom";
import React from "react";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import MixtapeDetails from "../components/MixtapeDetails";
import {Container, Divider, Fab, Stack, Typography} from "@mui/material";
import {Add as AddIcon, Info as InfoIcon} from "@mui/icons-material";
import useMixtape from "../hooks/useMixtape";
import {toast} from "react-toastify";
import SearchTrackForm from "../components/SearchTrackForm";
import useForm from "../hooks/useForm";
import MessageContainer from "../components/MessageContainer";
import Track from "../types/track";
import FlippableTrackCard from "../components/FlippableTrackCard";

const trackLimitPerMixtape: number = Number(process.env.REACT_APP_TRACK_LIMIT_PER_MIXTAPE);

export default function MixtapeDetailPage() {
    const navigate = useNavigate();
    const {id} = useParams<{id: string}>();

    const {mixtape, setMixtape, loading, error} = useMixtape(id);

    const {
        isFormOpen: isSearchTrackFormOpen,
        openForm: openSearchTrackForm,
        closeForm: closeSearchTrackForm
    } = useForm();

    const trackLimitReached = mixtape?.tracks && mixtape.tracks.length >= trackLimitPerMixtape;

    const navigateToMixtapesOverviewPage = () => {
        navigate("/mixtapes");
    }

    if (!loading && error) {
        toast.info(error);
        navigateToMixtapesOverviewPage();
    }

    if (!mixtape) {
        return null;
    }

    const addTrack = (savedTrack: Track) => {
       setMixtape({...mixtape, tracks: [...mixtape.tracks, savedTrack]});
    }

    const updateTrack = (updatedTrack: Track) => {
        setMixtape({...mixtape, tracks: mixtape.tracks.map(track => track.id === updatedTrack.id ? updatedTrack : track)});
    }

    const deleteTrack = (deletedTrack: Track) => {
        setMixtape({...mixtape, tracks: mixtape.tracks.filter(track => track.id !== deletedTrack.id)});
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

            {trackLimitReached &&
                <Typography sx={{display: "flex", alignItems: "center"}}>
                    <InfoIcon color="primary"/>
                    {`You reached the maximum number of ${trackLimitPerMixtape} tracks.`}
                </Typography>
            }

            <Stack spacing={2} sx={{width: "100%"}}>
                {mixtape.tracks.length === 0
                    ? <MessageContainer minHeight={200}>
                        <Typography>Your mixtape has no tracks yet.</Typography>
                        <Typography sx={{display: "flex", alignItems: "center"}}>
                            <InfoIcon color="primary"/>
                            {`You can add up to ${trackLimitPerMixtape} tracks.`}
                        </Typography>
                    </MessageContainer>
                    : mixtape.tracks.map(track => (
                        <FlippableTrackCard key={track.id} mixtape={mixtape} track={track} onEdit={updateTrack} onDelete={deleteTrack}/>
                    ))
                }
            </Stack>

            {!trackLimitReached &&
              <Fab color="primary" size="medium" onClick={openSearchTrackForm} aria-label="add track" sx={{
                  position: "fixed",
                  bottom: (theme) => theme.spacing(9),
                  right: (theme) => theme.spacing(2)
              }}>
                <AddIcon/>
              </Fab>
            }

            {isSearchTrackFormOpen && <SearchTrackForm mixtape={mixtape} open={isSearchTrackFormOpen} onSave={addTrack} onClose={closeSearchTrackForm}/>}
        </Container>
    );
}