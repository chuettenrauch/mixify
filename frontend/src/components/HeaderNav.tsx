import {
    AppBar,
    Avatar,
    Box,
    Container,
    IconButton,
    ListItemIcon,
    Menu,
    MenuItem,
    Toolbar,
} from "@mui/material";
import React from "react";
import {useUserContext} from "../context/userContext";
import {Logout as LogoutIcon} from "@mui/icons-material";
import {UserApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import logo from "../logo.png";

export default function HeaderNav() {
    const {user} = useUserContext();
    const navigate = useNavigate();

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);

    const onLogout = async () => {
        await UserApi.logout();

        toast.success("Successfully logged out.");

        navigate("/login");
    }

    return (
        <AppBar position="static">
            <Container maxWidth="md">
                <Toolbar disableGutters>
                    <Box sx={{ filter: "invert(1)", display: "flex", alignItems: "center"}}>
                        <img src={logo} width="150" alt="mixify logo"/>
                    </Box>

                    <Box sx={{ marginLeft: "auto" }}>
                        <IconButton
                            onClick={(e) => setAnchorEl(e.currentTarget)}
                            sx={{ p: 0 }}
                            aria-controls={open ? 'account-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={open ? 'true' : undefined}
                        >
                            <Avatar alt={user?.name} src={user?.imageUrl}/>
                        </IconButton>

                        <Menu
                            id="account-menu"
                            anchorEl={anchorEl}
                            open={open}
                            onClose={() => setAnchorEl(null)}
                            onClick={() => setAnchorEl(null)}
                        >
                            <MenuItem onClick={onLogout}>
                                <ListItemIcon>
                                    <LogoutIcon fontSize="small" />
                                </ListItemIcon>
                                Logout
                            </MenuItem>
                        </Menu>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}