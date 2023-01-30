import {Button, Dialog, DialogActions, DialogTitle} from "@mui/material";

export default function ConfirmDialog({open, title, onCancel, onConfirm}: {
    open: boolean,
    title: string,
    onCancel: () => void,
    onConfirm: () => void,
}) {
    return (
        <Dialog
            open={open}
            onClose={() => onCancel()}
            aria-labelledby="confirm-dialog-title"
            aria-describedby="confirm-dialog-description"
        >
            <DialogTitle id="confirm-dialog-title">{title}</DialogTitle>
            <DialogActions>
                <Button onClick={() => onCancel()}>Cancel</Button>
                <Button onClick={() => onConfirm()} autoFocus>Confirm</Button>
            </DialogActions>
        </Dialog>
    );
}