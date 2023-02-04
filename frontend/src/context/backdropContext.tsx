import React, {createContext, Dispatch, ReactNode, SetStateAction, useContext, useState} from "react";
import {Backdrop} from "@mui/material";

const BackdropContext = createContext<{backdrop: boolean, enableBackdrop: Dispatch<SetStateAction<boolean>>}>({
    backdrop: false,
    enableBackdrop: () => undefined
});

export function BackdropProvider({children}: {
    children: ReactNode|ReactNode[]
}) {
    const [backdrop, enableBackdrop] = useState<boolean>(false);

    return (
        <BackdropContext.Provider value={{backdrop, enableBackdrop}}>
            {children}

            {backdrop &&
              <Backdrop open={backdrop} sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}/>
            }
        </BackdropContext.Provider>
    )
}

export function useBackdrop() {
    return useContext(BackdropContext);
}