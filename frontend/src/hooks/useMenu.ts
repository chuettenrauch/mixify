import {useCallback, useState} from "react";

export default function useMenu() {
    const [menuAnchorEl, setMenuAnchorEl] = useState<null | HTMLElement>(null);

    const openMenu = useCallback((element: HTMLElement) => {
        setMenuAnchorEl(element);
    }, []);

    const closeMenu = useCallback(() => {
        setMenuAnchorEl(null);
    }, []);

    const isMenuOpen = Boolean(menuAnchorEl);

    return {menuAnchorEl, isMenuOpen, openMenu, closeMenu}
}