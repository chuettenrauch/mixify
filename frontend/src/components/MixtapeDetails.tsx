import Mixtape from "../types/mixtape";
import ConfirmDialog from "./ConfirmDialog";
import MixtapeForm from "./MixtapeForm";
import {
    Box,
    Container,
    IconButton, ListItemIcon, Menu, MenuItem,
    Typography
} from "@mui/material";
import React, {useEffect} from "react";
import useMenu from "../hooks/useMenu";
import useForm from "../hooks/useForm";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {MixtapeApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import MixtapeUtils from "../utils/mixtape-utils";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import CardImageWithPlayButton from "./CardImageWithPlayButton";
import {useBackdrop} from "../context/backdropContext";
import {useNavigate} from "react-router-dom";

export default function MixtapeDetails({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const navigate = useNavigate();
    const {menuAnchorEl: mixtapeMenuAnchorEl, isMenuOpen: isMixtapeMenuOpen, openMenu: openMixtapeMenu, closeMenu: closeMixtapeMenu} = useMenu();
    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();
    const {enableBackdrop} = useBackdrop();

    const {
        isConfirmDialogOpen: isDeleteConfirmDialogOpen,
        openConfirmDialog: openDeleteConfirmDialog,
        closeConfirmDialog: closeDeleteConfirmDialog
    } = useConfirmDialog();

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;

    useEffect(() => {
        enableBackdrop(isMixtapeMenuOpen);
    }, [enableBackdrop, isMixtapeMenuOpen]);

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
                    <CardImageWithPlayButton
                        image={{src: mixtape.imageUrl, alt: mixtape.title, size: 130}}
                        onClick={() => navigate(`/play/${mixtape.id}`)}
                    />
                </Box>

                <Container sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                    <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                </Container>

                <IconButton onClick={(e) => openMixtapeMenu(e.currentTarget)}
                            aria-controls={isMixtapeMenuOpen ? mixtapeMenuId : undefined}
                            aria-haspopup="true"
                            aria-expanded={isMixtapeMenuOpen ? 'true' : undefined}
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
                    anchorEl={mixtapeMenuAnchorEl}
                    open={isMixtapeMenuOpen}
                    onClose={closeMixtapeMenu}
                    onClick={closeMixtapeMenu}
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
            </Container>

            <Box>
                <Typography textAlign="justify">{mixtape.description}</Typography>
            </Box>
        </Container>
    );
}