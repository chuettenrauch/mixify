import Track from "../types/track";
import {
    Backdrop,
    Card,
    CardActions,
    CardContent,
    CardMedia,
    Container,
    IconButton, ListItemIcon, Menu, MenuItem,
    Typography
} from "@mui/material";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import ConfirmDialog from "./ConfirmDialog";
import React from "react";
import TrackForm from "./TrackForm";
import useMenu from "../hooks/useMenu";
import useForm from "../hooks/useForm";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {TrackApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import Mixtape from "../types/mixtape";

export default function TrackCard({mixtape, track, onEdit, onDelete}: {
    mixtape: Mixtape,
    track: Track,
    onEdit: (savedTrack: Track) => void,
    onDelete: (deletedTrack: Track) => void,
}) {
    const {menuAnchorEl: trackMenuAnchorEl, isMenuOpen: isTrackMenuOpen, openMenu: openTrackMenu, closeMenu: closeTrackMenu} = useMenu();
    const {isFormOpen: isTrackFormOpen, openForm: openTrackForm, closeForm: closeTrackForm} = useForm();

    const {
        isConfirmDialogOpen: isDeleteConfirmDialogOpen,
        openConfirmDialog: openDeleteConfirmDialog,
        closeConfirmDialog: closeDeleteConfirmDialog
    } = useConfirmDialog();

    const trackMenuId = `track-${track.id}-menu`;

    const handleDeleteConfirmed = async () => {
        await TrackApi.deleteTrack(mixtape, track);

        onDelete(track);
        toast.success("Successfully deleted track.");

        closeDeleteConfirmDialog();
    };

    return (
        <Card elevation={5} sx={{display: "flex", position: "relative"}}>
            <CardMedia
                component="img"
                image={track.imageUrl}
                alt={track.name}
                sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey"}}
            />

            <CardContent sx={{display: "flex", alignItems: "stretch"}}>
                <Container
                    sx={{display: "flex", flexDirection: "column", justifyContent: "space-between", p: 0}}>
                    <Container sx={{p: 0}}>
                        <Typography variant="h3">{track.name}</Typography>
                    </Container>
                </Container>
            </CardContent>

            <CardActions>
                <IconButton onClick={(e) => openTrackMenu(e.currentTarget)}
                            aria-controls={isTrackMenuOpen ? trackMenuId : undefined}
                            aria-haspopup="true"
                            aria-expanded={isTrackMenuOpen ? 'true' : undefined}
                            sx={{
                                alignSelf: "flex-start",
                                position: "absolute",
                                top: (theme) => theme.spacing(1),
                                right: 0,
                            }}>
                    <MoreVertIcon/>
                </IconButton>
                <Menu
                    id={trackMenuId}
                    anchorEl={trackMenuAnchorEl}
                    open={isTrackMenuOpen}
                    onClose={closeTrackMenu}
                    onClick={closeTrackMenu}
                >
                    <MenuItem onClick={openTrackForm}>
                        <ListItemIcon>
                            <EditIcon fontSize="small"/>
                        </ListItemIcon>
                        Edit
                    </MenuItem>
                    <MenuItem onClick={openDeleteConfirmDialog}>
                        <ListItemIcon>
                            <CloseIcon fontSize="small"/>
                        </ListItemIcon>
                        Delete
                    </MenuItem>
                </Menu>
            </CardActions>

            {isDeleteConfirmDialogOpen &&
              <ConfirmDialog
                open={isDeleteConfirmDialogOpen}
                title={`Do you really want to delete the track "${track.name}"?`}
                onCancel={closeDeleteConfirmDialog}
                onConfirm={handleDeleteConfirmed}
              />
            }

            {isTrackFormOpen &&
              <TrackForm
                title="Edit track"
                open={isTrackFormOpen}
                mixtape={mixtape}
                track={track}
                onSave={onEdit}
                onClose={closeTrackForm}
              />
            }

            {isTrackMenuOpen &&
              <Backdrop open={isTrackMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
            }
        </Card>
    );
}