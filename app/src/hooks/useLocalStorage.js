import { useState } from "react";

const useLocalStorage = () => {
    const [value, setValue] = useState(null)

    const setItem = (key, value) => {
        localStorage.setItem(key, value);
        setValue(value);
    }

    const getItem = (key) => {
        const value = localStorage.getItem(key);
        setValue(value);
        return value;
    }

    const removeItem = (key) => {
        localStorage.removeItem(key);
        setValue(null);
    }

    return { setItem, getItem, removeItem, value }
}

export default useLocalStorage;