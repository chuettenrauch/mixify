import {Navigate, Outlet, useLocation} from "react-router-dom";
import {createContext, Dispatch, SetStateAction, useContext, useEffect, useMemo, useState} from "react";
import {UserApi} from "../api/mixify-api";
import {AuthenticatedUser} from "../types/user";

type UserContextType = {
    user: AuthenticatedUser | null,
    setUser: Dispatch<SetStateAction<AuthenticatedUser | null>>
};

const UserContext = createContext<UserContextType>({
    user: null,
    setUser: () => undefined
});

export default function ProtectedRoutes() {
    const location = useLocation();
    const [user, setUser] = useState<AuthenticatedUser | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        (async () => {
            try {
                console.log("get new user from protected routes");
                const authenticatedUser = await UserApi.getAuthenticatedUser();
                setUser(authenticatedUser);
            } catch (e) {
                setUser(null);
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
            ? <UserContext.Provider value={userContext}>
                <Outlet/>
            </UserContext.Provider>
            : <Navigate to="/login"/>
    )
}

export function useAuthenticatedUser() {
    return useContext(UserContext);
}