import {IconButton, ListItemIcon, Menu, MenuItem} from "@mui/material";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import React from "react";
import useMenu from "../hooks/useMenu";
import useForm from "../hooks/useForm";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {TrackApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import ConfirmDialog from "./ConfirmDialog";
import TrackForm from "./TrackForm";
import Track from "../types/track";
import Mixtape from "../types/mixtape";

export default function TrackCardMenu({track, mixtape, onEdit, onDelete}: {
    track: Track,
    mixtape: Mixtape,
    onEdit: (savedTrack: Track) => void,
    onDelete: (deletedTrack: Track) => void,
}) {
    const {
        menuAnchorEl: trackMenuAnchorEl,
        isMenuOpen: isTrackMenuOpen,
        openMenu: openTrackMenu,
        closeMenu: closeTrackMenu
    } = useMenu();

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
        <>
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
        </>
    )
}