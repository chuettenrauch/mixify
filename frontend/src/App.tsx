import React from 'react';
import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {Container, CssBaseline} from "@mui/material";
import MixtapesOverviewPage from "./pages/MixtapesOverviewPage";
import PlayMixtapePage from "./pages/PlayMixtapePage";
import HeaderNav from "./components/HeaderNav";
import BottomNav from "./components/BottomNav";
import LoginPage from "./pages/LoginPage";
import MixtapeDetailPage from "./pages/MixtapeDetailPage";

function App() {
    return (
        <>
            <CssBaseline/>
            <BrowserRouter>
                <HeaderNav/>

                <Container maxWidth="md">
                    <Routes>
                        <Route path="/" element={<Navigate to="/mixtapes"/>}/> {/* redirect dashboard to mixtapes page for now, as the dashboard is optional */}
                        <Route path="/login" element={<LoginPage/>}/>
                        <Route path="/mixtapes" element={<MixtapesOverviewPage/>}/>
                        <Route path="/mixtapes/:id" element={<MixtapeDetailPage/>}/>
                        <Route path="/play/:id" element={<PlayMixtapePage/>}/>
                    </Routes>
                </Container>

                <BottomNav/>
            </BrowserRouter>
        </>
    );
}

export default App;
