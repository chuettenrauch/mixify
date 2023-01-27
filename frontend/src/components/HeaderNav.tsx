import {
    AppBar,
    Avatar, Backdrop,
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
    const {user, setUser} = useUserContext();
    const navigate = useNavigate();

    const [accountMenuAnchorEl, setAccountMenuAnchorEl] = React.useState<null | HTMLElement>(null);
    const accountMenuOpen = Boolean(accountMenuAnchorEl);

    const onLogout = async () => {
        await UserApi.logout();
        setUser(null);

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
                            onClick={(e) => setAccountMenuAnchorEl(e.currentTarget)}
                            sx={{ p: 0 }}
                            aria-controls={accountMenuOpen ? 'account-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={accountMenuOpen ? 'true' : undefined}
                        >
                            <Avatar alt={user?.name} src={user?.imageUrl}/>
                        </IconButton>

                        <Menu
                            id="account-menu"
                            anchorEl={accountMenuAnchorEl}
                            open={accountMenuOpen}
                            onClose={() => setAccountMenuAnchorEl(null)}
                            onClick={() => setAccountMenuAnchorEl(null)}
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
            <Backdrop open={accountMenuOpen} sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}/>
        </AppBar>
    );
}