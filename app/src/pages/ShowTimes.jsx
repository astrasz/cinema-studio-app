import { useEffect, useState } from "react";
import { redirect, useNavigate } from "react-router-dom";
import { getShowTimes } from "../api";

const ShowTimes = () => {

    const navigate = useNavigate();
    const [showTimes, setShowTimes] = useState([]);
    const [error, setError] = useState('');



    useEffect(() => {
        getShowTimes()
            .then(response => response.json())
            .then(data => {
                setShowTimes(data)
            })
            .catch(err => {
                setError(err.message)
            })
    }, [])

    const handleClick = (e, showId) => {
        e.preventDefault();
        return navigate(`/${showId}`)
    }

    return (
        <div>
            <table className="table show-times">
                <thead>
                    <tr className="text-center">
                        <th scope="col">No.</th>
                        <th scope="col">Date</th>
                        <th scope="col">Time</th>
                        <th scope="col">Movie</th>
                    </tr>
                </thead>
                <tbody>
                    {showTimes.length !== 0 && showTimes.map((show, index) => {
                        return (
                            <tr key={index} className={`${(index + 1) % 2 ? "table-danger" : "table-info even"} text-center`} onClick={(e) => handleClick(e, show.id)}>
                                <th scope="row">{index + 1}</th>
                                <td>{show.date}</td>
                                <td>{show.date}</td>
                                <td>{show.movieTitle}</td>
                            </tr>
                        )
                    })}
                </tbody>
            </table>
        </div>
    )
}

export default ShowTimes;