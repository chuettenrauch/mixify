import {BottomNavigation, BottomNavigationAction, Paper} from "@mui/material";
import {PlayCircle as PlayCircleIcon, QueueMusic as QueueMusicIcon} from "@mui/icons-material";
import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import localStorage from  "react-secure-storage";
import StorageKey from "../utils/local-storage-utils";

export default function BottomNav() {
    const navigate = useNavigate();
    const location = useLocation();

    const [value, setValue] = useState<string>(location.pathname);
    const [lastPlayUrl, setLastPlayUrl] = useState<string|null>();

    useEffect(() => {
        setValue(location.pathname);

        const lastPlayUrl = localStorage.getItem(StorageKey.LAST_PLAY_URL);
        if (lastPlayUrl) {
            setLastPlayUrl(String(lastPlayUrl));
        }
    }, [location])

    return (
        <Paper sx={{position: 'fixed', bottom: 0, left: 0, width: "100%", zIndex: (theme) => theme.zIndex.appBar - 1}} elevation={10}>
            <BottomNavigation
                value={value}
            >
                {lastPlayUrl &&
                  <BottomNavigationAction
                    label="Player"
                    icon={<PlayCircleIcon fontSize="large"/>}
                    value={lastPlayUrl}
                    onClick={() => navigate(lastPlayUrl)}
                  />
                }
                <BottomNavigationAction
                    label="Mixtapes"
                    icon={<QueueMusicIcon fontSize="large"/>}
                    value="/mixtapes"
                    onClick={() => navigate("/mixtapes")}
                />
            </BottomNavigation>
        </Paper>
    );
}