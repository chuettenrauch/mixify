import HeaderNav from "./HeaderNav";
import BottomNav from "./BottomNav";
import {Outlet} from "react-router-dom";
import {Box, Container} from "@mui/material";
import {WebPlaybackSDK} from "react-spotify-web-playback-sdk";
import {useUserContext} from "../context/userContext";
import {useCallback} from "react";
import {BackdropProvider} from "../context/backdropContext";

export default function MainLayout() {
    const {user} = useUserContext();

    const getAccessToken = useCallback((callback: (token: string) => void) => {
        if (!user) {
            return;
        }

        callback(user.providerAccessToken);
    }, [user]);

    return (
        <BackdropProvider>
            <Box sx={{maxHeight: window.innerHeight, overflow: "scroll"}}>
                {user &&
                    /* @ts-ignore */
                    <WebPlaybackSDK
                      initialDeviceName="Mixify"
                      getOAuthToken={getAccessToken}
                      initialVolume={0.5}
                      connectOnInitialized={true}
                    >
                      <HeaderNav/>

                      <Container maxWidth="md" sx={{p: 2, mb: 10, minHeight: window.innerHeight}}>
                        <Outlet/>
                      </Container>

                      <BottomNav/>
                    </WebPlaybackSDK>
                }
            </Box>
        </BackdropProvider>
    );
}