import {IconButton, ListItemIcon, Menu, MenuItem} from "@mui/material";
import UserAvatar from "../UserAvatar";
import {Logout as LogoutIcon, Settings as SettingsIcon} from "@mui/icons-material";
import React, {useEffect} from "react";
import useMenu from "../../hooks/useMenu";
import {useBackdrop} from "../../context/backdropContext";
import {useAuthenticatedUser} from "../ProtectedRoutes";
import {useNavigate} from "react-router-dom";
import {UserApi} from "../../api/mixify-api";
import {toast} from "react-toastify";

export default function AccountMenu() {
    const {user, setUser} = useAuthenticatedUser();
    const navigate = useNavigate();

    const {menuAnchorEl, isMenuOpen, openMenu, closeMenu} = useMenu();
    const {enableBackdrop} = useBackdrop();

    useEffect(() => {
        enableBackdrop(isMenuOpen);
    }, [isMenuOpen, enableBackdrop])

    const onLogout = async () => {
        await UserApi.logout();
        setUser(null);

        toast.success("Successfully logged out.");

        navigate("/login");
    }

    return (
        <>
            {user &&
                <IconButton
                    onClick={(e) => openMenu(e.currentTarget)}
                    sx={{p: 0}}
                    aria-controls={isMenuOpen ? 'account-menu' : undefined}
                    aria-haspopup="true"
                    aria-expanded={isMenuOpen ? 'true' : undefined}
                >
                    <UserAvatar user={user}/>
                </IconButton>
            }

            <Menu
                id="account-menu"
                anchorEl={menuAnchorEl}
                open={isMenuOpen}
                onClose={closeMenu}
                onClick={closeMenu}
            >
                <MenuItem onClick={() => navigate("/settings")}>
                    <ListItemIcon>
                        <SettingsIcon fontSize="small"/>
                    </ListItemIcon>
                    Settings
                </MenuItem>
                <MenuItem onClick={onLogout}>
                    <ListItemIcon>
                        <LogoutIcon fontSize="small" />
                    </ListItemIcon>
                    Logout
                </MenuItem>
            </Menu>
        </>
    )
}