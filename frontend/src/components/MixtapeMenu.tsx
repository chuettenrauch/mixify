import {IconButton, ListItemIcon, Menu, MenuItem} from "@mui/material";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import React, {useEffect} from "react";
import useMenu from "../hooks/useMenu";
import useForm from "../hooks/useForm";
import {useBackdrop} from "../context/backdropContext";
import useConfirmDialog from "../hooks/useConfirmDialog";
import ConfirmDialog from "./ConfirmDialog";
import Mixtape from "../types/mixtape";
import MixtapeForm from "./MixtapeForm";
import {MixtapeApi} from "../api/mixify-api";
import {toast} from "react-toastify";

export default function MixtapeMenu({mixtape, onDelete, onEdit}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const {menuAnchorEl: mixtapeAnchorEl, isMenuOpen: isMixtapeMenuOpen, openMenu: openMixtapeMenu, closeMenu: closeMixtapeMenu} = useMenu();
    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();
    const {enableBackdrop} = useBackdrop();

    const {
        isConfirmDialogOpen: isDeleteConfirmDialogOpen,
        openConfirmDialog: openDeleteConfirmDialog,
        closeConfirmDialog: closeDeleteConfirmDialog
    } = useConfirmDialog();

    useEffect(() => {
        enableBackdrop(isMixtapeMenuOpen);
    }, [enableBackdrop, isMixtapeMenuOpen]);

    const handleDeleteConfirmed = async () => {
        await MixtapeApi.deleteMixtape(mixtape);

        onDelete(mixtape);
        toast.success("Successfully deleted mixtape.");

        closeDeleteConfirmDialog();
    };

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;

    return (
        <>
            <IconButton onClick={(e) => openMixtapeMenu(e.currentTarget)}
                        aria-controls={isMixtapeMenuOpen ? mixtapeMenuId : undefined}
                        aria-haspopup="true"
                        aria-expanded={isMixtapeMenuOpen ? 'true' : undefined}
                        sx={{
                            alignSelf: "flex-start",
                            position: "absolute",
                            top: (theme) => theme.spacing(1),
                            right: 0,
                        }}>
                <MoreVertIcon/>
            </IconButton>
            <Menu
                id={mixtapeMenuId}
                anchorEl={mixtapeAnchorEl}
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
        </>
    )
}