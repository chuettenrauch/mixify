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
import React, {useCallback, useState} from "react";
import MixtapeUtils from "../utils/mixtape-utils";
import MixtapeForm from "./MixtapeForm";
import ConfirmDialog from "./ConfirmDialog";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {MixtapeApi} from "../api/mixify-api";
import {toast} from "react-toastify";

export default function MixtapeCard({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const [mixtapeMenu, setMixtapeMenu] = useState<null | HTMLElement>(null);
    const [isMixtapeFormOpen, setIsMixtapeFormOpen] = useState(false);

    const {isConfirmDialogOpen: isDeleteConfirmDialogOpen, openConfirmDialog: openDeleteConfirmDialog, closeConfirmDialog: closeDeleteConfirmDialog} = useConfirmDialog();

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;
    const mixtapeMenuOpen = Boolean(mixtapeMenu);

    const openMixtapeForm = () => {
        setIsMixtapeFormOpen(true);
    }

    const closeMixtapeForm = () => {
        setIsMixtapeFormOpen(false);
    }

    const handleDeleteConfirmed = useCallback(async () => {
        await MixtapeApi.deleteMixtape(mixtape);

        onDelete(mixtape);
        closeDeleteConfirmDialog();

        toast.success("Successfully deleted mixtape.");
    }, [onDelete, mixtape, closeDeleteConfirmDialog]);

    return (
        <Card elevation={5} sx={{display: "flex", position: "relative"}}>
            <CardActionArea sx={{display: "flex", justifyContent: "flex-start", alignItems: "stretch", p: 2}}>
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
                <IconButton onClick={(e) => setMixtapeMenu(e.currentTarget)}
                            aria-controls={mixtapeMenuOpen ? mixtapeMenuId : undefined}
                            aria-haspopup="true"
                            aria-expanded={mixtapeMenuOpen ? 'true' : undefined}
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
                    anchorEl={mixtapeMenu}
                    open={mixtapeMenuOpen}
                    onClose={() => setMixtapeMenu(null)}
                    onClick={() => setMixtapeMenu(null)}
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
                title={`Do you really want to delete the mixtape "${mixtape.title}"?`}
                onCancel={closeDeleteConfirmDialog}
               onConfirm={handleDeleteConfirmed}
            />

            <MixtapeForm title="Edit mixtape" open={isMixtapeFormOpen} mixtape={mixtape} onSave={onEdit}
                         onClose={closeMixtapeForm}/>
            <Backdrop open={mixtapeMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
        </Card>
    );
}