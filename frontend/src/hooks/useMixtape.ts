import {useEffect, useState} from "react";
import Mixtape from "../types/mixtape";
import {MixtapeApi} from "../api/mixify-api";
import axios from "axios";

export default function useMixtape(id: string|undefined) {
    const [mixtape, setMixtape] = useState<Mixtape|null>(null)
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string|null>();

    useEffect(() => {
        if (!id) {
            return;
        }

        (async () => {
            try {
                const mixtape: Mixtape = await MixtapeApi.getMixtape(id);
                setMixtape(mixtape);
            } catch(e: any) {
                let errorMsg = "Something went wrong.";

                if (axios.isAxiosError(e) && e.response?.status === 404) {
                    errorMsg = "Mixtape doesn't exist.";
                }

                setError(errorMsg)
            } finally {
                setLoading(false);
            }
        })();
    }, [id]);

    return {mixtape, setMixtape, loading, error};
}