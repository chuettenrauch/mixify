import {Navigate, Outlet} from "react-router-dom";
import {useUserContext} from "../context/userContext";
import {useEffect, useState} from "react";
import {UserApi} from "../api/mixify-api";

export default function ProtectedRoutes() {
    const {user, setUser} = useUserContext();
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        (async () => {
            try {
                const authenticatedUser = await UserApi.getAuthenticatedUser();
                setUser(authenticatedUser);
            } catch (e) {
                // do nothing
            } finally {
                setLoading(false);
            }
        })();
    }, [setUser]);

    if (loading) {
        return null;
    }

    return (
        user
            ? <Outlet/>
            : <Navigate to="/login"/>
    )
}