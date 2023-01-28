import {BottomNavigation, BottomNavigationAction, Paper} from "@mui/material";
import {PlayCircle as PlayCircleIcon, QueueMusic as QueueMusicIcon} from "@mui/icons-material";
import React from "react";
import {useLocation, useNavigate} from "react-router-dom";

export default function BottomNav() {
    const navigate = useNavigate();
    const location = useLocation();
    const [value, setValue] = React.useState(location.pathname);

    return (
        <Paper sx={{position: 'fixed', bottom: 0, left: 0, right: 0, zIndex: (theme) => theme.zIndex.appBar}} elevation={10}>
            <BottomNavigation
                value={value}
                onChange={(event, index) => {
                    setValue(index);
                }}
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