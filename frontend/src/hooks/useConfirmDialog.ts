import {useCallback, useState} from "react";

export default function useConfirmDialog() {
    const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false);

    const openConfirmDialog = useCallback(() => {
        setIsConfirmDialogOpen(true);
    }, []);

    const closeConfirmDialog = useCallback(() => {
        setIsConfirmDialogOpen(false);
    }, []);

    return {isConfirmDialogOpen, openConfirmDialog, closeConfirmDialog};
}