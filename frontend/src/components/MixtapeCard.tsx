import Mixtape from "../types/mixtape";
import {
    Box,
    Card, CardActionArea, CardActions, CardContent,
    Container,
    Typography
} from "@mui/material";
import MixtapeUtils from "../utils/mixtape-utils";
import {Link, useNavigate} from "react-router-dom";
import CardImageWithPlayButton from "./CardImageWithPlayButton";
import MixtapeMenu from "./MixtapeMenu";
import UserAvatar from "./UserAvatar";
import {useAuthenticatedUser} from "./ProtectedRoutes";
import PermissionUtils from "../utils/permission-utils";

export default function MixtapeCard({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const {user} = useAuthenticatedUser();
    const navigate = useNavigate();

    const canEdit = PermissionUtils.canEdit(user, mixtape);

    return (
        <Card elevation={5} sx={{display: "flex", position: "relative"}}>
            <CardActions sx={{p: 2, pr: 0}}>
                <CardImageWithPlayButton
                    image={{src: mixtape.imageUrl, alt: mixtape.title, size: 100}}
                    onClick={() => navigate(`/play/${mixtape.id}`)}
                />
            </CardActions>

            <CardActionArea component={Link} to={canEdit ? `/mixtapes/${mixtape.id}` : `/play/${mixtape.id}`} sx={{
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

                            {user?.id !== mixtape.createdBy.id
                                ? <Box sx={{display: "flex", alignItems: "center"}}>
                                    <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)} by</Typography>
                                    <UserAvatar user={mixtape.createdBy}
                                                sx={{mr: 0.5, ml: 0.5, width: 20, height: 20}}/>
                                </Box>
                                : <Typography>{MixtapeUtils.formatCreatedAt(mixtape.createdAt)}</Typography>
                            }

                        </Container>
                        <Typography>{MixtapeUtils.formatNumberOfTracks(mixtape.tracks)}</Typography>
                    </Container>
                </CardContent>
            </CardActionArea>

            <CardActions>
                {canEdit &&
                  <MixtapeMenu mixtape={mixtape} onEdit={onEdit} onDelete={onDelete}/>
                }
            </CardActions>
        </Card>
    );
}