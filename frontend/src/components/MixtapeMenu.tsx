import {IconButton, ListItemIcon, Menu, MenuItem, SxProps, Theme} from "@mui/material";
import {
    Close as CloseIcon,
    Edit as EditIcon,
    MoreVert as MoreVertIcon,
    Share as ShareIcon, SportsScore
} from "@mui/icons-material";
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
import useOpenClose from "../hooks/useOpenClose";
import ShareModal from "./ShareModal";
import {useAuthenticatedUser} from "./ProtectedRoutes";
import PermissionUtils from "../utils/permission-utils";

export default function MixtapeMenu({mixtape, onDelete, onEdit, sx}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
    sx?: SxProps<Theme>
}) {
    const {user} = useAuthenticatedUser();

    const {menuAnchorEl: mixtapeAnchorEl, isMenuOpen: isMixtapeMenuOpen, openMenu: openMixtapeMenu, closeMenu: closeMixtapeMenu} = useMenu();
    const {isFormOpen: isMixtapeFormOpen, openForm: openMixtapeForm, closeForm: closeMixtapeForm} = useForm();
    const {isOpen: isShareModalOpen, open: openShareModal, close: closeShareModal} = useOpenClose();
    const {enableBackdrop} = useBackdrop();

    const {
        isConfirmDialogOpen: isDeleteConfirmDialogOpen,
        openConfirmDialog: openDeleteConfirmDialog,
        closeConfirmDialog: closeDeleteConfirmDialog
    } = useConfirmDialog();

    const {
        isConfirmDialogOpen: isFinalizeConfirmDialogOpen,
        openConfirmDialog: openFinalizeConfirmDialog,
        closeConfirmDialog: closeFinalizeConfirmDialog
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

    const handleFinalizeConfirmed = async() => {
        const savedMixtape = await MixtapeApi.updateMixtape({...mixtape, draft: false});
        onEdit(savedMixtape);

        toast.success("Successfully finalizeed mixtape.");

        closeFinalizeConfirmDialog();
    }

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;

    const isEditable = PermissionUtils.isEditable(user, mixtape);
    const isShareable = PermissionUtils.isShareable(user, mixtape);

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
                            ...sx,
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
                {isEditable &&
                  <MenuItem onClick={openMixtapeForm}>
                    <ListItemIcon>
                      <EditIcon fontSize="small"/>
                    </ListItemIcon>
                    Edit
                  </MenuItem>
                }
                {isShareable &&
                  <MenuItem onClick={openShareModal}>
                    <ListItemIcon>
                      <ShareIcon fontSize="small"/>
                    </ListItemIcon>
                    Share
                  </MenuItem>
                }
                {isEditable &&
                  <MenuItem onClick={openFinalizeConfirmDialog}>
                    <ListItemIcon>
                      <SportsScore fontSize="small"/>
                    </ListItemIcon>
                    Finalize
                  </MenuItem>
                }
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
                title={`Do you really want to delete your mixtape "${mixtape.title}"?`}
                onCancel={closeDeleteConfirmDialog}
                onConfirm={handleDeleteConfirmed}
              />
            }

            {isFinalizeConfirmDialogOpen &&
              <ConfirmDialog
                open={isFinalizeConfirmDialogOpen}
                title={`Do you really want to finalize your mixtape "${mixtape.title}"?`}
                text="You can't edit it afterwards anymore."
                onCancel={closeFinalizeConfirmDialog}
                onConfirm={handleFinalizeConfirmed}
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

            {isShareModalOpen &&
              <ShareModal open={isShareModalOpen} mixtape={mixtape} onClose={closeShareModal}/>
            }
        </>
    )
}