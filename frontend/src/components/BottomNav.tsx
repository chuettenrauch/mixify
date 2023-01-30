import {BottomNavigation, BottomNavigationAction, Paper} from "@mui/material";
import {PlayCircle as PlayCircleIcon, QueueMusic as QueueMusicIcon} from "@mui/icons-material";
import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";

export default function BottomNav() {
    const navigate = useNavigate();
    const location = useLocation();

    const [value, setValue] = useState<string>(location.pathname);

    useEffect(() => {
        setValue(location.pathname);
    }, [location])

    return (
        <Paper sx={{position: 'fixed', bottom: 0, left: 0, right: 0, zIndex: (theme) => theme.zIndex.appBar - 1}} elevation={10}>
            <BottomNavigation
                value={value}
            >
                <BottomNavigationAction
                    label="Player"
                    icon={<PlayCircleIcon fontSize="large"/>}
                    value="/play"
                    onClick={() => navigate("/play")}
                />
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