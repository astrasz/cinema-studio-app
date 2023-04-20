import { createContext, useEffect, useState } from "react";
import useLocalStorage from "../hooks/useLocalStorage";

export const AuthContext = createContext({
    user: null,
    setUser: (user) => { }
})

export const AuthContextProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const { getItem } = useLocalStorage()

    useEffect(() => {
        const userAuth = getItem('user')
        if (userAuth) {
            const data = JSON.parse(userAuth);
            setUser(data);
        }
    }, [])

    return (
        <AuthContext.Provider value={{ user, setUser }}>
            {children}
        </AuthContext.Provider>
    )
}