import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Admin from './pages/Admin';
import Movies from './pages/Movies';
import ShowTimes from './pages/ShowTimes';
import ShowTimeSeats from './pages/ShowTimeSeats';


function App() {

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
                        element={<Admin />}
                    ></Route>
                </Routes>
            </Layout>
        </BrowserRouter>
    )

}

export default App;