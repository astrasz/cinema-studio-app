import { Link } from "react-router-dom";
import Home from "../pages/ShowTimes";

const Layout = ({ children }) => {
    return (
        <>
            <div className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container">
                    <a href="../" className="navbar-brand">Cinema Studio</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarResponsive">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <Link className="nav-link" to="/">Show Times</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/movies">Movies</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/admin">Admin</Link>

                            </li>
                        </ul>
                        {/* <ul className="navbar-nav ms-md-auto">
                            <li className="nav-item">
                                <a target="_blank" rel="noopener" className="nav-link" href="https://github.com/thomaspark/bootswatch/"><i className="bi bi-github"></i> GitHub</a>
                            </li>
                            <li className="nav-item">
                                <a target="_blank" rel="noopener" className="nav-link" href="https://twitter.com/bootswatch"><i className="bi bi-twitter"></i> Twitter</a>
                            </li>
                        </ul> */}
                    </div>
                </div>
            </div>

            <main className="flex-shrink-0">
                <div className="container mt-4">
                    {children}
                </div>
            </main>
        </>
    )
}

export default Layout;