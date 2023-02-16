import {useNavigate, useParams} from "react-router-dom";
import React from "react";
import PageHeader from "../components/PageHeader";
import Mixtape from "../types/mixtape";
import MixtapeDetails from "../components/MixtapeDetails";
import {Box, Divider, Fab, Typography} from "@mui/material";
import {Add as AddIcon, Info as InfoIcon} from "@mui/icons-material";
import useMixtape from "../hooks/useMixtape";
import {toast} from "react-toastify";
import SearchTrackForm from "../components/SearchTrackForm";
import useForm from "../hooks/useForm";
import MessageContainer from "../components/MessageContainer";
import Track from "../types/track";
import {useAuthenticatedUser} from "../components/ProtectedRoutes";
import PermissionUtils from "../utils/permission-utils";
import NotFoundPage from "./NotFoundPage";
import SortableTrackList from "../components/SortableTrackList";
import {MixtapeApi} from "../api/mixify-api";
import jsonpatch, {MovePatch} from "json-patch";

const trackLimitPerMixtape: number = Number(process.env.REACT_APP_TRACK_LIMIT_PER_MIXTAPE);

export default function MixtapeDetailPage() {
    const navigate = useNavigate();
    const {id} = useParams<{id: string}>();
    const {user} = useAuthenticatedUser();

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

    if (!PermissionUtils.canEdit(user, mixtape)) {
        return <NotFoundPage/>
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

    const moveTrack = (sourceIndex: number, destinationIndex: number) => {
        (async () => {
            const movePatch: MovePatch = {
                op: "move",
                from: `/tracks/${sourceIndex}`,
                path: `/tracks/${destinationIndex}`
            };

            const patchedMixtape = jsonpatch.apply(mixtape, [movePatch]);
            setMixtape(patchedMixtape);

            await MixtapeApi.updateMixtape(patchedMixtape);
        })();
    }

    return (
        <Box sx={{
            display: "flex",
            flexDirection: "column",
            gap: 3,
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

            {mixtape.tracks.length === 0
                ? <MessageContainer minHeight={200}>
                    <Typography>Your mixtape has no tracks yet.</Typography>
                    <Typography sx={{display: "flex", alignItems: "center"}}>
                        <InfoIcon color="primary"/>
                        {`You can add up to ${trackLimitPerMixtape} tracks.`}
                    </Typography>
                </MessageContainer>
                : <SortableTrackList
                    mixtape={mixtape}
                    onUpdateTrack={updateTrack}
                    onDeleteTrack={deleteTrack}
                    onMoveTrack={moveTrack}
                />
            }

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
        </Box>
    );
}