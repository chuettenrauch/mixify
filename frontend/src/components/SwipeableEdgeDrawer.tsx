import * as React from "react";
import { Global } from "@emotion/react";
import CssBaseline from "@mui/material/CssBaseline";
import { grey } from "@mui/material/colors";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";
import {IconButton, Paper} from "@mui/material";
import {KeyboardArrowDown, KeyboardArrowUp} from "@mui/icons-material";

const drawerBleeding = 110;

export default function SwipeableEdgeDrawer() {
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
                        height: `calc(69vh - ${drawerBleeding}px)`,
                        overflow: "visible", // tried to set to scroll and auto
                    },
                }}
            />
            <SwipeableDrawer
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
                        marginTop: `${-drawerBleeding + 5}px`,
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
                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                        <Typography textTransform="uppercase" sx={{p: 2}}>Show tracks</Typography>
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
                        maxHeight: `calc(70vh - ${drawerBleeding}px)`,
                        overflow: "auto",
                        p: 2,
                    }}>
                        <Typography>
                            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
                            Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
                            Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.
                            Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer
                        </Typography>
                    </Box>
                </Paper>
            </SwipeableDrawer>
        </Box>
    );
}
