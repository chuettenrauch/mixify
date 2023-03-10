import HeaderNav from "./HeaderNav";
import BottomNav from "./BottomNav";
import {Outlet} from "react-router-dom";
import {Box, Container} from "@mui/material";
import {WebPlaybackSDK} from "react-spotify-web-playback-sdk";
import {useCallback} from "react";
import {BackdropProvider} from "../context/backdropContext";
import {GlobalConfigProvider} from "../context/globalConfigContext";
import {useAuthenticatedUser} from "./ProtectedRoutes";

export default function MainLayout() {
    const {user} = useAuthenticatedUser();

    const getAccessToken = useCallback((callback: (token: string) => void) => {
        if (!user) {
            return;
        }

        callback(user.accessToken);
    }, [user]);

    return (
        <BackdropProvider>
            <Box sx={{display: "flex", flexDirection: "column", overflow: "hidden", height: "100dvh"}}>
                {user &&
                    /* @ts-ignore */
                  <WebPlaybackSDK
                    initialDeviceName="Mixify"
                    getOAuthToken={getAccessToken}
                    initialVolume={0.5}
                    connectOnInitialized={true}
                  >
                    <GlobalConfigProvider>
                      <HeaderNav/>

                      <Container maxWidth="sm" sx={{p: 2, pb: 20, minHeight: "100dvh", overflow: "auto"}}>
                        <Outlet/>
                      </Container>

                      <BottomNav/>
                    </GlobalConfigProvider>
                  </WebPlaybackSDK>
                }
            </Box>
        </BackdropProvider>
    );
}