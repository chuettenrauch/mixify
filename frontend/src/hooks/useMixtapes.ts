import {Dispatch, SetStateAction, useEffect, useState} from "react";
import Mixtape from "../types/mixtape";
import {MixtapeApi} from "../api/mixify-api";

export default function useMixtapes(): [Mixtape[], Dispatch<SetStateAction<Mixtape[]>>] {
    const [mixtapes, setMixtapes] = useState<Mixtape[]>([]);

    useEffect(() => {
        (async () => {
            const mixtapes = await MixtapeApi.getMixtapes();
            setMixtapes(mixtapes);
        })();
    }, []);

    return [mixtapes, setMixtapes];
}