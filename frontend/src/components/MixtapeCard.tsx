import Mixtape from "../types/mixtape";
import {
    Backdrop,
    Card, CardActionArea, CardActions, CardContent,
    Container,
    IconButton,
    ListItemIcon,
    Menu,
    MenuItem,
    Typography
} from "@mui/material";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import React from "react";
import MixtapeUtils from "../utils/mixtape-utils";
import MixtapeForm from "./MixtapeForm";
import ConfirmDialog from "./ConfirmDialog";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {MixtapeApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import useForm from "../hooks/useForm";
import useMenu from "../hooks/useMenu";
import {Link} from "react-router-dom";
import CardImage from "./CardImage";

export default function MixtapeCard({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const {menuAnchorEl: mixtapeAnchorEl, isMenuOpen: isMixtapeMenuOpen, openMenu: openMixtapeMenu, closeMenu: closeMixtapeMenu} = useMenu();
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
        <Card elevation={5} sx={{display: "flex", position: "relative"}}>
            <CardActions sx={{p: 2, pr: 0}}>
                <CardImage image={{src: mixtape.imageUrl, alt: mixtape.title, size: 100}} link={`/play/${mixtape.id}`}/>
            </CardActions>

            <CardActionArea component={Link} to={`/mixtapes/${mixtape.id}`} sx={{
                display: "flex",
                justifyContent: "flex-start",
                alignItems: "stretch",
                p: 0,
                paddingBlock: 2
            }}>
                <CardContent sx={{display: "flex", alignItems: "stretch"}}>
                    <Container
                        sx={{display: "flex", flexDirection: "column", justifyContent: "space-between", p: 0}}>
                        <Container sx={{p: 0}}>
                            <Typography variant="h3">{mixtape.title}</Typography>
                            <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                        </Container>
                        <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                    </Container>
                </CardContent>
            </CardActionArea>

            <CardActions>
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
            </CardActions>

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

            {isMixtapeMenuOpen &&
              <Backdrop open={isMixtapeMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
            }
        </Card>
    );
}