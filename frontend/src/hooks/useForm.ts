import {useCallback, useState} from "react";

export default function useForm() {
    const [isFormOpen, setIsFormOpen] = useState(false);

    const openForm = useCallback(() => {
        setIsFormOpen(true);
    }, []);

    const closeForm = useCallback(() => {
        setIsFormOpen(false);
    }, []);

    return {isFormOpen, openForm, closeForm};
}