import Mixtape from "../types/mixtape";
import {
    Backdrop,
    Card, CardActionArea, CardActions, CardContent, CardMedia,
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

export default function MixtapeCard({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const {menuAnchorEl, isMenuOpen, openMenu, closeMenu} = useMenu();
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
            <CardActionArea component={Link} to={`/mixtapes/${mixtape.id}`} sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}}>
                <CardMedia
                    component="img"
                    image={`/api/files/${mixtape.image}`}
                    alt={mixtape.title}
                    sx={{width: 100, height: 100, lineHeight: 0, border: "1px solid grey"}}
                />

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
                <IconButton onClick={(e) => openMenu(e.currentTarget)}
                            aria-controls={isMenuOpen ? mixtapeMenuId : undefined}
                            aria-haspopup="true"
                            aria-expanded={isMenuOpen ? 'true' : undefined}
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
                    anchorEl={menuAnchorEl}
                    open={isMenuOpen}
                    onClose={closeMenu}
                    onClick={closeMenu}
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

            <ConfirmDialog
                open={isDeleteConfirmDialogOpen}
                title={`Do you really want to delete your "${mixtape.title}" mixtape?`}
                onCancel={closeDeleteConfirmDialog}
                onConfirm={handleDeleteConfirmed}
            />

            <MixtapeForm
                title="Edit mixtape"
                open={isMixtapeFormOpen}
                mixtape={mixtape}
                onSave={onEdit}
                onClose={closeMixtapeForm}
            />

            <Backdrop open={isMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
        </Card>
    );
}