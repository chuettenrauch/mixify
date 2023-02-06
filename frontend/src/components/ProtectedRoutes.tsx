import {Navigate, Outlet, useLocation, useOutletContext} from "react-router-dom";
import {Dispatch, SetStateAction, useEffect, useMemo, useState} from "react";
import {UserApi} from "../api/mixify-api";
import {AuthenticatedUser} from "../types/user";

type UserContext = {
    user: AuthenticatedUser | null,
    setUser: Dispatch<SetStateAction<AuthenticatedUser | null>>
};

export default function ProtectedRoutes() {
    const location = useLocation();
    const [user, setUser] = useState<AuthenticatedUser | null>(null);
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
    }, [location]);

    const userContext = useMemo(() => {
        return {user, setUser}
    }, [user, setUser]);

    if (loading) {
        return null;
    }

    return (
        user
            ? <Outlet context={userContext}/>
            : <Navigate to="/login"/>
    )
}

export function useAuthenticatedUser() {
    return useOutletContext<UserContext>();
}