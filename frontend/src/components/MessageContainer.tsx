import {Container} from "@mui/material";
import {ReactNode} from "react";

export default function MessageContainer({minHeight = window.innerHeight / 2, children}: {
    minHeight?: string|number
    children: ReactNode | ReactNode[]
}) {
    return (
        <Container sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            minHeight: minHeight,
        }}>
            {children}
        </Container>
    )
}