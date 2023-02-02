import {Avatar, SxProps} from "@mui/material";
import React from "react";
import User from "../types/user";

export default function UserAvatar({user, sx = {}}: {
    user: User,
    sx?: SxProps
}) {
    return (
        <Avatar alt={user.name} src={user.imageUrl} sx={sx}>
            {user.name[0].toUpperCase()}
        </Avatar>
    )
}