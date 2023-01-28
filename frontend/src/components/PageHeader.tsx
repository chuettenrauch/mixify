import {Container, IconButton, Typography} from "@mui/material";
import {ChevronLeft as ChevronLeftIcon} from "@mui/icons-material";
import {useNavigate} from "react-router-dom";

export default function PageHeader({title}: {
    title: string
}) {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    }

    return (
        <Container sx={{display: "flex", justifyContent: "center", p: 0, position: "relative"}}>
            <IconButton sx={{position: "absolute", left: 0, top: 0}} onClick={goBack}>
                <ChevronLeftIcon/>
            </IconButton>
            <Typography variant="h1" component="h1" textTransform={"uppercase"}>{title}</Typography>
        </Container>
    )
}