import {useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from  "react-secure-storage";
import {useEffect} from "react";
import StorageKey from "../utils/local-storage-utils";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    useEffect(() => {
        if (!id) {
            return;
        }

        localStorage.setItem(StorageKey.LAST_PLAY_URL, location.pathname);
    }, [location, id]);

    // put all tracks to spotify queue

    return (
        <>
            <h1>Play mixtape</h1>
            {mixtape &&
              <p>{mixtape.title}</p>
            }
        </>
    );
}