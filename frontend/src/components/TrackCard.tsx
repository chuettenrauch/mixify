import Track from "../types/track";
import {Typography} from "@mui/material";

export default function TrackCard({track}: {
    track: Track
}) {
    return (
      <>
          <Typography>{track.id}</Typography>
      </>
    );
}