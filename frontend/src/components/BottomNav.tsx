import {BottomNavigation, BottomNavigationAction, Paper} from "@mui/material";
import {PlayCircle as PlayCircleIcon, QueueMusic as QueueMusicIcon} from "@mui/icons-material";
import React from "react";

export default function BottomNav() {
    return (
        <Paper sx={{position: 'fixed', bottom: 0, left: 0, right: 0}} elevation={3}>
            <BottomNavigation>
                {/*<BottomNavigationAction label="Dashboard" icon={<HomeIcon fontSize="large"/>} />*/}
                <BottomNavigationAction label="Player" icon={<PlayCircleIcon fontSize="large"/>}/>
                <BottomNavigationAction label="Mixtapes" icon={<QueueMusicIcon fontSize="large"/>}/>
            </BottomNavigation>
        </Paper>
    );
}