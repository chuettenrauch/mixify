import User from "../types/user";
import {createContext, Dispatch, ReactNode, SetStateAction, useContext, useState} from "react";

const UserContext = createContext<{user: User|null, setUser: Dispatch<SetStateAction<User|null>>}>({
    user: null,
    setUser: () => undefined
});

export function UserProvider({children}: {
    children: ReactNode|ReactNode[]
}) {
    const [user, setUser] = useState<User|null>(null);

    return (
        <UserContext.Provider value={{user, setUser}}>
            {children}
        </UserContext.Provider>
    )
}

export function useUserContext() {
    return useContext(UserContext);
}