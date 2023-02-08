import {
    Button, Container, IconButton,
    Modal,
    Paper,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {
    Close as CloseIcon,
    ContentCopy as ContentCopyIcon,
    Info as InfoIcon,
} from "@mui/icons-material";
import React, {useEffect, useState} from "react";
import {CopyToClipboard} from "react-copy-to-clipboard";
import Mixtape from "../types/mixtape";
import {InviteApi} from "../api/mixify-api";
import InviteUtils from "../utils/invite-utils";
import {toast} from "react-toastify";

export default function ShareModal({open, mixtape, onClose}: {
    open: boolean,
    mixtape: Mixtape,
    onClose: () => void,
}) {
    const [shareLink, setShareLink] = useState<string | null>();

    useEffect(() => {
        (async () => {
            const invite = await InviteApi.createInvite({mixtape: mixtape.id});

            const shareLink = InviteUtils.createShareLinkFromInvite(invite);
            setShareLink(shareLink);
        })();
    }, [mixtape.id]);

    const onCopyLink = () => {
        toast.success("Copied link.");
    }

    if (!shareLink) {
        return null;
    }

    return (
        <Modal
            open={open}
            onClose={onClose}
            aria-labelledby="modal-share"
            aria-describedby="Modal to share mixtape"
            sx={{zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Paper elevation={5} sx={{
                position: "absolute",
                top: '30%',
                left: '50%',
                transform: 'translate(-50%, -30%)',
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 2,
                width: "min(85%, 450px)",
                p: 2
            }}>
                <Container sx={{display: "flex", justifyContent: "center", position: "relative"}}>
                    <Typography variant="h2" textTransform={"uppercase"}>Share mixtape</Typography>
                    <IconButton sx={{position: "absolute", top: -8, right: -8}} onClick={() => onClose()}>
                        <CloseIcon/>
                    </IconButton>
                </Container>

                <Stack sx={{
                    gap: 2,
                    p: 1
                }}>
                    <Typography>Give this link to your friends to share your mixtape with them:</Typography>
                    <TextField
                        disabled
                        value={shareLink}
                        sx={{width: "100%"}}
                    />
                    <Typography sx={{display: "flex", alignItems: "center", marginInline: "auto"}}>
                        <InfoIcon color="primary"/>This link is active for 1h.
                    </Typography>

                    <CopyToClipboard text={shareLink} onCopy={onCopyLink}>
                        <Button variant="contained" startIcon={<ContentCopyIcon/>}>Copy link</Button>
                    </CopyToClipboard>
                </Stack>
            </Paper>
        </Modal>
    )
}