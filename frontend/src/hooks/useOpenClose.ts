import {useCallback, useState} from "react";

export default function useOpenClose() {
    const [isOpen, setIsOpen] = useState(false);

    const open = useCallback(() => {
        setIsOpen(true);
    }, []);

    const close = useCallback(() => {
        setIsOpen(false);
    }, []);

    return {isOpen, open, close};
}