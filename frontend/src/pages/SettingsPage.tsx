import {useAuthenticatedUser} from "../components/ProtectedRoutes";
import PageHeader from "../components/PageHeader";
import React from "react";
import {Box, Button, Stack} from "@mui/material";
import {Clear as ClearIcon} from "@mui/icons-material";
import ConfirmDialog from "../components/ConfirmDialog";
import useConfirmDialog from "../hooks/useConfirmDialog";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import {UserApi} from "../api/mixify-api";

export default function SettingsPage() {
    const navigate = useNavigate()
    const {setUser} = useAuthenticatedUser();

    const {
        isConfirmDialogOpen: isDeleteAccountConfirmDialogOpen,
        openConfirmDialog: openDeleteAccountConfirmDialog,
        closeConfirmDialog: closeDeleteAccountConfirmDialog
    } = useConfirmDialog();

    const handleDeleteAccountConfirmed = async () => {
        await UserApi.deleteUser();

        toast.success("Successfully deleted account.");
        setUser(null);

        navigate("/login");
    }

    return (
        <Box sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
        }}>
            <PageHeader title="Settings"/>

            <Stack
                spacing={2}
                width="100%"
                maxWidth={(theme) => theme.breakpoints.values.sm}
            >
                <Button
                    variant="contained"
                    startIcon={<ClearIcon/>}
                    color="error"
                    onClick={openDeleteAccountConfirmDialog}
                >
                    Delete Account
                </Button>
            </Stack>

            {isDeleteAccountConfirmDialogOpen &&
                <ConfirmDialog
                    open={isDeleteAccountConfirmDialogOpen}
                    title={`Do you really want to delete your account?`}
                    onCancel={closeDeleteAccountConfirmDialog}
                    onConfirm={handleDeleteAccountConfirmed}
                />
            }
        </Box>
    )
}