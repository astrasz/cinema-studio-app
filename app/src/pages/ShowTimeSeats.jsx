import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { bookManyTickets, getShowTimeById } from "../api";
import Row from "../components/Row";
import Seat from "../components/Seat";
import SeatsLegend from "../components/SeatsLegend";
import TicketsSummary from "../components/TicketsSummary";
import ModalTip from "../components/ModalTip";
import { useAuthContext } from "../hooks/useAuthContext";

const ShowTimeSeats = () => {
    const params = useParams();
    const context = useAuthContext();

    const [showTime, setShowTime] = useState("");
    const [seatsByRows, setSeatsByRows] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState([])
    const [isDiscount, setIsDiscount] = useState(false);
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

    const addToSelectedSeats = (seat) => {
        const newSelected = [...selectedSeats];
        newSelected.push(seat);
        setSelectedSeats(newSelected);
    }

    const removeFromSelectedSeats = (seat) => {
        const newSelected = [...selectedSeats].filter(selectedSeat => selectedSeat.chair !== seat.chair)
        setSelectedSeats(newSelected);
    }

    const toggleDiscount = () => setIsDiscount((prevState) => !prevState);

    const bookSeats = () => {

        if (!context.user) {
            return;
        }

        const bookingList = selectedSeats.map(seat => {
            return {
                seatId: seat.seatId,
                userId: "d44702a6-fc02-4735-9991-f550f9e86f52",
                typeId: isDiscount ? seat.halfPriceId : seat.regularId
            }
        })
        bookManyTickets(JSON.stringify(bookingList))
            .then(response => response.json())
            .then(data => {
                console.log(data);
            })
            .catch(err => {
                setError(err.message)
            })
    }

    return (

        <div className="seats-container text-center mx-auto" style={{ maxWidth: "1000px" }}>
            <p className="mt-5 mb-3 h1">{showTime && showTime.movieTitle}</p>
            <p className="mb-5 h4 text-danger">{showTime && showTime.date}</p>

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
                                        selectedSeats={selectedSeats}
                                        addToSelected={addToSelectedSeats}
                                        removeFromSelected={removeFromSelectedSeats}
                                    />
                                })}
                            </div>
                        )
                        }
                    </div>
                    <div className="col-1"></div>
                    <div className="col-2 d-flex flex-column justify-content-center align-items-start">
                        <SeatsLegend />
                    </div>
                </div>
                <div className="row w-100 my-5">
                    <div className="summary w-100 mt-5">
                        <TicketsSummary
                            selectedSeats={selectedSeats}
                            isDiscount={isDiscount}
                            removeFromSelected={removeFromSelectedSeats}
                            toggleDiscount={toggleDiscount}
                            bookSeats={bookSeats}
                        />
                    </div>
                </div>
            </div>
        </div>
    )

}

export default ShowTimeSeats;