import HeaderNav from "../components/HeaderNav";
import BottomNav from "../components/BottomNav";
import {Outlet} from "react-router-dom";
import {Container} from "@mui/material";

export default function MainLayout() {
    return (
        <>
            <HeaderNav/>

            <Container maxWidth="md" sx={{p: 5}}>
                <Outlet/>
            </Container>

            <BottomNav/>
        </>
    );
}