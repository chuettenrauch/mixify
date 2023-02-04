import {AuthenticatedUser} from "../types/user";
import {createContext, Dispatch, ReactNode, SetStateAction, useContext, useMemo, useState} from "react";

const UserContext = createContext<{user: AuthenticatedUser|null, setUser: Dispatch<SetStateAction<AuthenticatedUser|null>>}>({
    user: null,
    setUser: () => undefined
});

export function UserProvider({children}: {
    children: ReactNode|ReactNode[]
}) {
    const [user, setUser] = useState<AuthenticatedUser|null>(null);

    const userContext = useMemo(() => {
        return {user, setUser}
    }, [user, setUser]);

    return (
        <UserContext.Provider value={userContext}>
            {children}
        </UserContext.Provider>
    )
}

export function useUserContext() {
    return useContext(UserContext);
}