import * as React from "react";
import { Global } from "@emotion/react";
import CssBaseline from "@mui/material/CssBaseline";
import { grey } from "@mui/material/colors";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";
import {IconButton, Paper} from "@mui/material";
import {KeyboardArrowDown, KeyboardArrowUp} from "@mui/icons-material";
import {ReactNode} from "react";

export default function SwipeableEdgeDrawer({title, drawerBleeding, children}: {
    title: string,
    drawerBleeding: number,
    children: ReactNode | ReactNode[],
}) {
    const [open, setOpen] = React.useState(false);

    const container = window !== undefined ? () => window.document.body : undefined;

    return (
        <Box sx={{
            height: "100%",
        }}>
            <CssBaseline />
            <Global
                styles={{
                    ".MuiDrawer-root > .MuiPaper-root": {
                        height: `calc(85vh - ${drawerBleeding}px)`,
                        overflow: "visible", // tried to set to scroll and auto
                    },
                    ".MuiBox-root .PrivateSwipeArea-root": {
                        zIndex: 1000
                    }
                }}
            />
            <SwipeableDrawer
                data-drawer
                container={container}
                anchor="bottom"
                open={open}
                onClose={() => setOpen(false)}
                onOpen={() => setOpen(true)}
                swipeAreaWidth={drawerBleeding}
                disableSwipeToOpen={false}
                ModalProps={{
                    keepMounted: true,
                }}
                sx={{
                    zIndex: (theme) => theme.zIndex.appBar - 2,
                }}
            >
                <Paper
                    data-drawer-handle
                    variant="outlined"
                    sx={{
                        position: "relative",
                        marginTop: `${-drawerBleeding}px`,
                        borderTopLeftRadius: 8,
                        borderTopRightRadius: 8,
                        visibility: "visible",
                        right: 0,
                        left: 0,
                        backgroundColor: grey[300],
                    }}
                >
                    {/* Pull Handle */}
                    <Box sx={{
                        width: 30,
                        height: 6,
                        backgroundColor: grey[600],
                        borderRadius: 3,
                        position: "absolute",
                        top: 8,
                        left: "calc(50% - 15px)",
                    }} />
                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3}}>
                        <Typography textTransform="uppercase" sx={{p: 2}}>{title}</Typography>
                        {open
                            ? <IconButton size="large" onClick={() => setOpen(false)}>
                                <KeyboardArrowDown fontSize="inherit"/>
                            </IconButton>
                            : <IconButton size="large" onClick={() => setOpen(true)}>
                                <KeyboardArrowUp fontSize="inherit"/>
                            </IconButton>
                        }
                    </Box>
                    <Box sx={{
                        height: `calc(85vh - ${drawerBleeding}px)`,
                        maxHeight: `calc(85vh - ${drawerBleeding}px)`,
                        overflow: "auto",
                        p: 2,
                    }}>
                        {children}
                    </Box>
                </Paper>
            </SwipeableDrawer>
        </Box>
    );
}
