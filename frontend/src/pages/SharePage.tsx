import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";
import {InviteApi} from "../api/mixify-api";
import {toast} from "react-toastify";
import {isAxiosError} from "axios";

export default function SharePage() {
    const navigate = useNavigate();
    const {id} = useParams<{id: string}>();

    useEffect(() => {
        if (!id) {
            return;
        }

        (async () => {
            try {
                const mixtapeUser = await InviteApi.acceptInvite(id);
                navigate(`/mixtapes/${mixtapeUser.mixtape.id}`, {replace: true});

                toast.success(`Added the mixtape "${mixtapeUser.mixtape.title}" to your collection.`);
            } catch (e) {
                if (isAxiosError(e) && e.response) {
                    switch (e.response.status) {
                        case 410:
                            navigate("/", {replace: true});

                            toast.error("Link is expired.");
                            break;
                        case 404:
                            navigate("/not-found", {replace: true});
                    }
                }
            }

        })();
    }, [id, navigate]);

    return null;
}