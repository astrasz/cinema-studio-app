import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getShowTimeById } from "../api";

const ShowTimeSeats = () => {
    const params = useParams();

    const [seats, setSeats] = useState();
    const [error, setError] = useState();

    useEffect(() => {
        const { showTimeId } = params;

        getShowTimeById(showTimeId)
            .then(response => response.json())
            .then(data => {
                setSeats(data)
            })
            .catch(err => {
                setError(err.message)
            })
    }, [])

    return (
        <div className="seats-container">
            {seats && <h1>{seats.movieTitle}</h1>}

        </div>
    )

}

export default ShowTimeSeats;