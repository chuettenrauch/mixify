import {Box, Card, CardMedia, Container, IconButton, Typography} from "@mui/material";
import {Redo as RedoIcon} from "@mui/icons-material";
import ReactCardFlip from "react-card-flip";
import React, {ReactNode, useState} from "react";
import Image from "../types/image";

export default function FlippableImageCard({image, textOnBack, children}: {
    image: Image,
    textOnBack: string
    children: ReactNode
}) {
    const [isImageFlipped, setIsImageFlipped] = useState<boolean>(false);

    return (
        <ReactCardFlip isFlipped={isImageFlipped} flipSpeedBackToFront={1} flipSpeedFrontToBack={1} containerStyle={{width: "100%"}}>
            <Card elevation={10} sx={{display: "flex", flexDirection: "column", width: "100%", p: 2, pb: 0}}>
                <Container sx={{paddingTop: "100%", position: "relative"}}>
                    <CardMedia
                        component="img"
                        image={image.src}
                        alt={image.alt}
                        sx={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            objectFit: "cover",
                            width: "100%",
                            height: "100%",
                            border: "1px solid grey",
                        }}
                    />
                    {children}
                </Container>

                <IconButton onClick={() => setIsImageFlipped(!isImageFlipped)} sx={{marginLeft: "auto"}}>
                    <RedoIcon/>
                </IconButton>
            </Card>

            <Card elevation={10} square={true} sx={{display: "flex", flexDirection: "column", width: "100%", p: 2, pb: 0}}>
                <Container sx={{paddingTop: "100%", position: "relative"}}>
                    <CardMedia
                        component="img"
                        image={image.src}
                        alt={image.alt}
                        sx={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            objectFit: "cover",
                            width: "100%",
                            height: "100%",
                            border: "1px solid grey",
                            opacity: 0.3,
                        }}
                    />
                    <Box sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        position: "absolute",
                        top: 0,
                        left: 0,
                        width: "100%",
                        height: "100%",
                        p: 2,
                    }}>
                        <Typography variant="h2" textAlign="justify">{textOnBack}</Typography>
                    </Box>
                </Container>

                <IconButton onClick={() => setIsImageFlipped(!isImageFlipped)} sx={{marginLeft: "auto"}}>
                    <RedoIcon/>
                </IconButton>
            </Card>
        </ReactCardFlip>
    )
}