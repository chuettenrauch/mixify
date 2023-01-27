import HeaderNav from "./HeaderNav";
import BottomNav from "./BottomNav";
import {Outlet} from "react-router-dom";
import {Container} from "@mui/material";

export default function MainLayout() {
    return (
        <>
            <HeaderNav/>

            <Container maxWidth="md" sx={{p: 2}}>
                <Outlet/>
            </Container>

            <BottomNav/>
        </>
    );
}