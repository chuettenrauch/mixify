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
import Track from "../types/track";

const formatDate = (date: string) => {
    const formattedDate: Date = new Date(date);

    return formattedDate.toLocaleDateString("en", {month: "long", year: "numeric"})
}

const formatTrackNumberLabel = (tracks: Track[]) => {
    const numText: string = tracks.length === 0 ? "No" : String(tracks.length);
    const trackText: string = tracks.length === 1 ? "track" : "tracks";

    return `${numText} ${trackText}`;
}

export default function MixtapeCard({mixtape}: {
    mixtape: Mixtape
}) {
    const [mixtapeMenu, setMixtapeMenu] = React.useState<null | HTMLElement>(null);

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;
    const mixtapeMenuOpen = Boolean(mixtapeMenu);

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
                            <Typography>{formatDate(mixtape.createdAt)}</Typography>
                        </Container>
                        <Typography>{formatTrackNumberLabel(mixtape.tracks)}</Typography>
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
                    <MenuItem>
                        <ListItemIcon>
                            <EditIcon fontSize="small"/>
                        </ListItemIcon>
                        Edit
                    </MenuItem>
                    <MenuItem>
                        <ListItemIcon>
                            <CloseIcon fontSize="small"/>
                        </ListItemIcon>
                        Delete
                    </MenuItem>
                </Menu>
            </CardActions>
            <Backdrop open={mixtapeMenuOpen} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
        </Card>
    );
}