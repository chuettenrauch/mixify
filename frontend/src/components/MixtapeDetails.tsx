import Mixtape from "../types/mixtape";
import ConfirmDialog from "./ConfirmDialog";
import MixtapeForm from "./MixtapeForm";
import {
    Backdrop, Box,
    CardMedia, Container,
    IconButton, ListItemIcon, Menu, MenuItem,
    Typography
} from "@mui/material";
import React from "react";
import useMenu from "../hooks/useMenu";
import useForm from "../hooks/useForm";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {MixtapeApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import MixtapeUtils from "../utils/mixtape-utils";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";

export default function MixtapeDetails({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const {menuAnchorEl: trackMenuAnchorEl, isMenuOpen: isTrackMenuOpen, openMenu: openTrackMenu, closeMenu: closeTrackMenu} = useMenu();
    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();

    const {
        isConfirmDialogOpen: isDeleteConfirmDialogOpen,
        openConfirmDialog: openDeleteConfirmDialog,
        closeConfirmDialog: closeDeleteConfirmDialog
    } = useConfirmDialog();

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;

    const handleDeleteConfirmed = async () => {
        await MixtapeApi.deleteMixtape(mixtape);

        onDelete(mixtape);
        toast.success("Successfully deleted mixtape.");

        closeDeleteConfirmDialog();
    };

    return (
        <Container sx={{display: "flex", flexDirection: "column", gap: 2, p: 0}}>
            <Container sx={{display: "flex", width: "100%", p: 0, position: "relative"}}>
                <Box sx={{flex: 1}}>
                    <CardMedia
                        component="img"
                        image={mixtape.imageUrl}
                        alt={mixtape.title}
                        sx={{width: 130, height: 130, lineHeight: 0, border: "1px solid grey", flex: 1}}
                    />
                </Box>

                <Container sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                    <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                </Container>

                <IconButton onClick={(e) => openTrackMenu(e.currentTarget)}
                            aria-controls={isTrackMenuOpen ? mixtapeMenuId : undefined}
                            aria-haspopup="true"
                            aria-expanded={isTrackMenuOpen ? 'true' : undefined}
                            sx={{
                                alignSelf: "flex-start",
                                position: "absolute",
                                top: "-8px",
                                right: "-16px",
                            }}>
                    <MoreVertIcon/>
                </IconButton>
                <Menu
                    id={mixtapeMenuId}
                    anchorEl={trackMenuAnchorEl}
                    open={isTrackMenuOpen}
                    onClose={closeTrackMenu}
                    onClick={closeTrackMenu}
                >
                    <MenuItem onClick={openMixtapeForm}>
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
                    title={`Do you really want to delete your "${mixtape.title}" mixtape?`}
                    onCancel={closeDeleteConfirmDialog}
                    onConfirm={handleDeleteConfirmed}
                  />
                }

                {isMixtapeFormOpen &&
                  <MixtapeForm
                    title="Edit mixtape"
                    open={isMixtapeFormOpen}
                    mixtape={mixtape}
                    onSave={onEdit}
                    onClose={closeMixtapeForm}
                  />
                }

                {isTrackMenuOpen &&
                  <Backdrop open={isTrackMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
                }
            </Container>

            <Box>
                <Typography textAlign="justify">{mixtape.description}</Typography>
            </Box>
        </Container>
    );
}