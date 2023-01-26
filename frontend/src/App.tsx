import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {CssBaseline} from "@mui/material";
import MixtapesOverviewPage from "./pages/MixtapesOverviewPage";
import PlayMixtapePage from "./pages/PlayMixtapePage";
import LoginPage from "./pages/LoginPage";
import MixtapeDetailPage from "./pages/MixtapeDetailPage";
import MainLayout from "./components/MainLayout";
import ProtectedRoutes from "./components/ProtectedRoutes";
import {UserProvider} from "./context/userContext";

function App() {
    return (
        <UserProvider>
            <CssBaseline/>
            <BrowserRouter>
                <Routes>
                    <Route element={<ProtectedRoutes/>}>
                        <Route element={<MainLayout/>}>
                            <Route path="/" element={<Navigate to="/mixtapes"/>}/> {/* redirect dashboard to mixtapes page for now, as the dashboard is optional */}
                            <Route path="/mixtapes" element={<MixtapesOverviewPage/>}/>
                            <Route path="/mixtapes/:id" element={<MixtapeDetailPage/>}/>
                            <Route path="/play/:id" element={<PlayMixtapePage/>}/>
                        </Route>
                    </Route>

                    <Route path="/login" element={<LoginPage/>}/>
                </Routes>
            </BrowserRouter>
        </UserProvider>
    );
}

export default App;
