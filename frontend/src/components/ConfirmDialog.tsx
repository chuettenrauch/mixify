import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";

export default function ConfirmDialog({open, title, text, onCancel, onConfirm}: {
    open: boolean,
    title: string,
    text?: string,
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

            {text &&
              <DialogContent>
                <DialogContentText id="confirm-dialog-description">
                    {text}
                </DialogContentText>
              </DialogContent>
            }

            <DialogActions>
                <Button onClick={() => onCancel()}>Cancel</Button>
                <Button onClick={() => onConfirm()} autoFocus>Confirm</Button>
            </DialogActions>
        </Dialog>
    );
}