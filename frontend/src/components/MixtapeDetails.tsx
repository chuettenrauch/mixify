import Mixtape from "../types/mixtape";
import {
    Box,
    Container,
    Typography
} from "@mui/material";
import MixtapeUtils from "../utils/mixtape-utils";
import CardImageWithPlayButton from "./CardImageWithPlayButton";
import {useNavigate} from "react-router-dom";
import MixtapeMenu from "./MixtapeMenu";

export default function MixtapeDetails({mixtape, onEdit, onDelete}: {
    mixtape: Mixtape,
    onEdit: (savedMixtape: Mixtape) => void,
    onDelete: (deletedMixtape: Mixtape) => void,
}) {
    const navigate = useNavigate();

    return (
        <Box sx={{display: "flex", flexDirection: "column", gap: 2}}>
            <Box sx={{display: "flex", width: "100%", position: "relative"}}>
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

                <MixtapeMenu mixtape={mixtape} onEdit={onEdit} onDelete={onDelete} sx={{top: (theme) => theme.spacing(-1), right: (theme) => theme.spacing(-2)}}/>
            </Box>

            <Box>
                <Typography textAlign="justify">{mixtape.description}</Typography>
            </Box>
        </Box>
    );
}