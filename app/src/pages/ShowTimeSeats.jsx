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
            setSeatsByRows(seatsRows);
        }
    }, [showTime])


    return (

        <div className="seats-container text-center mx-auto" style={{ maxWidth: "1000px" }}>
            <h1 className="my-5">{showTime && showTime.movieTitle}</h1>

            <div className="container">
                <div className="row flex-nowrap">
                    <div className="col-9" style={{ width: "700px" }}>
                        <h4 className="text-warning">Screen</h4>
                        <div className="screen mb-4 mx-auto" style={{ width: "580px", height: "1rem", backgroundColor: "#fff", borderRadius: "5px" }}></div>
                        {showTime && (
                            <div style={{ width: "680px" }}  >
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
                    <div className="col-1"></div>
                    <div className="col-2 d-flex flex-column justify-content-center align-items-start">

                        <div className="resume"></div>

                        <div className="mb-3">
                            <h4>Legend</h4>
                        </div>
                        <div className="w-100 mb-3">
                            <div className="w-25"><p>Sold</p></div>
                            <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow bg-primary" style={{ width: "3rem", height: "3.5rem", border: "5px solid black" }} >
                            </div>
                        </div>
                        <div className="w-100 mb-3">
                            <div className="w-25"><p >Free</p></div>
                            <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow" style={{ width: "3rem", height: "3.5rem", border: "5px solid black", marginRight: "0" }} >
                            </div>
                        </div>
                        <div className="w-100 mb-3">
                            <div className="w-25"><p >Selected</p></div>
                            <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow bg-warning" style={{ width: "3rem", height: "3.5rem", border: "5px solid black", marginRight: "0" }} >
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default ShowTimeSeats;