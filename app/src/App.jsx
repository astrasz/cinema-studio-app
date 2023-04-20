import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Admin from './pages/Admin';
import Movies from './pages/Movies';
import ShowTimes from './pages/ShowTimes';
import ShowTimeSeats from './pages/ShowTimeSeats';
import { useAuthContext } from './hooks/useAuthContext';
import jwtDecode from 'jwt-decode';
import { useEffect, useState } from 'react';


function App() {
    const [role, setRole] = useState();

    const { user } = useAuthContext();

    useEffect(() => {
        if (user) {
            const decodedUser = jwtDecode(user.access_token);
            setRole(decodedUser.role);
        }

    }, [user])

    return (
        <BrowserRouter>
            <Layout>
                <Routes>
                    <Route
                        path='/'
                        element={<ShowTimes />}
                    >
                    </Route>
                    <Route
                        path='/:showTimeId'
                        element={<ShowTimeSeats />}
                    ></Route>
                    <Route
                        path='/movies'
                        element={<Movies />}
                    ></Route>
                    <Route
                        path='/admin'
                        element={!!user && role === 'ADMIN' ? <Admin /> : <Navigate to="/" />}
                    ></Route>
                </Routes>
            </Layout>
        </BrowserRouter>
    )

}

export default App;