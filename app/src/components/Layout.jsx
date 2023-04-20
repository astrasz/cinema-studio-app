import { Link, useNavigate } from "react-router-dom";
import Home from "../pages/ShowTimes";
import { useAuthContext } from "../hooks/useAuthContext";
import LoginModal from "./LogInModal";
import RegisterModal from "./RegisterModal";
import useLocalStorage from "../hooks/useLocalStorage";
import { logout } from "../api";
import { useEffect, useState } from "react";
import { useAuth } from "../hooks/useAuth";
import jwtDecode from "jwt-decode";

const Layout = ({ children }) => {

    const navigate = useNavigate();

    const context = useAuthContext();
    const [role, setRole] = useState("");
    const [error, setError] = useState();
    const [response, setResponse] = useState();
    const { logOut } = useAuth()

    useEffect(() => {
        if (context && context.user) {
            setRole(jwtDecode(context.user.access_token).role);
        }

    }, [context])

    const handleLogout = () => {
        logout()
            .then(response => {
                if (!response.ok) {
                    setError("Something went wrong, try again");
                    return;
                }
                logOut();
                setResponse("Successfuly logged out");
            })
            .catch(err => {
                setError(err.message)
            })
        navigate("/");
    }

    const handleCloseInfo = () => {
        handleCloseError();
        handleCloseResponse();
    }

    const handleCloseResponse = () => setResponse(null);
    const handleCloseError = () => setError(null);

    return (
        <>
            <div className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container">
                    <Link to="/" className="navbar-brand text-primary fw-bold bg-warning py-0 px-3" style={{ fontFamily: "Bebas Neue", fontSize: "3rem", borderRadius: "10px" }}>Cinema Studio</Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse ms-4" id="navbarResponsive">
                        <ul className="navbar-nav fs-5" onClick={handleCloseError}>
                            <li className="nav-item">
                                <Link className="nav-link" to="/">Show Times</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/movies">Movies</Link>
                            </li>
                            {!!context.user && role === "ADMIN" && (
                                <li className="nav-item">
                                    <Link className="nav-link" to="/admin">Add Movie</Link>

                                </li>
                            )}

                        </ul>

                        <ul className="navbar-nav ms-md-auto" onClick={handleCloseInfo}>
                            {!context.user && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link text-warning fw-bold" type="button" data-bs-toggle="modal" data-bs-target="#loginModal">Log In</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link text-warning fw-bold" type="button" data-bs-toggle="modal" data-bs-target="#registerModal">Register</Link>
                                    </li>
                                </>
                            )}
                            {context.user && (<li className="nav-item">
                                <Link onClick={handleLogout} className="nav-link text-warning fw-bold">Logout</Link>
                            </li>)}

                        </ul>
                    </div>
                </div>
            </div>

            <main className="flex-shrink-0">
                <div className="container mt-4">
                    {error &&
                        <div className="text-center d-flex justify-content-center mb-4">
                            <div className="d-flex">
                                <p className="p-2 m-0">{error}</p>
                                <button onClick={handleCloseError} className="btn btn-sm btn-outline-light ms-4" >
                                    <span onClick={handleCloseError} className="pb-0 m-0">X</span>
                                </button>
                            </div>
                        </div>
                    }
                    {response &&
                        <div className="text-center d-flex justify-content-center mb-4">
                            <div className="d-flex">
                                <p className="p-2 m-0">{response}</p>
                                <button onClick={handleCloseResponse} className="btn btn-sm btn-outline-light ms-4" >
                                    <span onClick={handleCloseResponse} className="pb-0 m-0">X</span>
                                </button>
                            </div>
                        </div>
                    }
                    {children}
                </div>
                {!context.user && (
                    <>
                        <RegisterModal />
                        <LoginModal />
                    </>
                )}
            </main>
        </>
    )
}

export default Layout;