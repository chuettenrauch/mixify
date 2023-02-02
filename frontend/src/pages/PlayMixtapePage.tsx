import {Location, useLocation, useParams} from "react-router-dom";
import useMixtape from "../hooks/useMixtape";
import localStorage from "react-secure-storage";
import {useEffect} from "react";
import StorageKey from "../utils/local-storage-utils";
import useMixtapePlayer from "../hooks/useMixtapePlayer";

export default function PlayMixtapePage() {
    const location = useLocation();
    const {id} = useParams<{ id: string }>();
    const {mixtape} = useMixtape(id);

    const {playerReady, addTracksToPlayer, startPlayer, pausePlayer} = useMixtapePlayer();

    useEffect(() => {
        updateLastPlayUrlInLocalStorage(location);
    }, [location]);

    useEffect(() => {
        if (!playerReady || !mixtape) {
            return;
        }

        (async() => {
            const uris = mixtape.tracks.map(track => track.providerUri);
            await addTracksToPlayer(uris)
        })();
    }, [playerReady, addTracksToPlayer, mixtape])

    // ensure that player is stopped, if user leaves the page
    useEffect(() => {
        return () => {
            (async () => {
                await pausePlayer();
            })();
        }
    }, [pausePlayer])

    return (
        <>
            <h1>Play mixtape</h1>
            {mixtape &&
              <p>{mixtape.title}</p>
            }

            {playerReady &&
              <>
                <p>Player is ready for use</p>
                <button onClick={() => startPlayer()}>Resume</button>
                <button onClick={() => pausePlayer()}>Pause</button>
              </>
            }
        </>
    );
}

function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}