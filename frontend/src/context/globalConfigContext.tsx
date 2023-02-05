import React, {createContext, ReactNode, useContext, useEffect, useMemo} from "react";
import {useErrorState} from "react-spotify-web-playback-sdk";
import {toast} from "react-toastify";

const GlobalConfigContext = createContext<{canUsePlayer: boolean}>({
    canUsePlayer: true,
});

export function GlobalConfigProvider({children}: {
    children: ReactNode|ReactNode[]
}) {
    const error = useErrorState();

    const globalConfigContext = useMemo(() => {
        return {
            canUsePlayer: !(error && error.type === "account_error"),
        }
    }, [error]);

    useEffect(() => {
        if (!globalConfigContext.canUsePlayer) {
            toast.error("Mixify requires a spotify premium account. You'll experience limited functionality.", {
                position: "top-center",
                autoClose: false,
                hideProgressBar: false,
                closeOnClick: true,
                draggable: false,
                theme: "colored",
            });
        }
    }, [globalConfigContext.canUsePlayer])

    return (
        <GlobalConfigContext.Provider value={globalConfigContext}>
            {children}
        </GlobalConfigContext.Provider>
    )
}

export function useGlobalConfig() {
    return useContext(GlobalConfigContext);
}