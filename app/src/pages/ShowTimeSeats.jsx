import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getShowTimeById } from "../api";
import Row from "../components/Row";
import Seat from "../components/Seat";

const ShowTimeSeats = () => {
    const params = useParams();

    const [showTime, setShowTime] = useState("");
    const [seatsByRows, setSeatsByRows] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        const { showTimeId } = params;

        getShowTimeById(showTimeId)
            .then(response => response.json())
            .then(data => {
                setShowTime(data)
            })
            .catch(err => {
                setError(err.message)
            })
    }, [])

    useEffect(() => {

        if (showTime) {
            let rows = [];
            showTime.seats.forEach(seat => {
                rows.push(seat.row);
            });
            rows = [...new Set(rows)]
            const rowsNumber = Math.max.apply(this, [...rows]);
            const { seats } = showTime;
            const seatsRows = [];
            for (let i = 0; i < rowsNumber; i++) {
                const row = seats.filter(seat => +seat.row === i + 1).sort((a, b) => a.chair - b.chair);
                seatsRows.push(row);
            }
            // console.log('seatsRows', seatsRows);
            setSeatsByRows(seatsRows);
        }
    }, [showTime])


    return (
        <div className="seats-container">
            <h1>{showTime && showTime.movieTitle}</h1>

            {showTime && (
                <div className="container mt-5" >
                    {seatsByRows.length !== 0 && seatsByRows.map((row, index) => {
                        return <Row
                            key={index}
                            seats={row}
                        />
                    })}
                </div>
            )
            }


        </div>
    )

}

export default ShowTimeSeats;