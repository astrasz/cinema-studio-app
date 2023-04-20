import jwt_decode from 'jwt-decode';
import { useContext } from "react";
import { AuthContext } from "../context/auth-context";
import useLocalStorage from "./useLocalStorage";

export const useAuth = () => {

    const { setUser } = useContext(AuthContext)
    const { setItem, removeItem } = useLocalStorage();


    const logIn = (user) => {
        setUser(user);
        setItem('user', JSON.stringify(user));

    }

    const logOut = () => {
        setUser(null);
        removeItem('user');
    }

    const refreshAuthorization = (user) => {
        setUser(user)
        setItem('user', JSON.stringify(user));
    }

    return { logIn, logOut, refreshAuthorization };
}