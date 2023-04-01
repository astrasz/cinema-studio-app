import { useEffect, useState } from "react";

const Seat = ({ seat, selectedSeats, addToSelected, removeFromSelected }) => {

    const [bgColor, setBgColor] = useState("");
    const [cursor, setCursor] = useState("");

    useEffect(() => {
        seat.state === 'SOLD' && setBgColor("text-primary");
        seat.state !== 'SOLD' && setCursor("pointer");
    }, [bgColor, cursor, seat.state])

    useEffect(() => {
        const seatIndex = selectedSeats.findIndex(selecteSeat => selecteSeat.chair === seat.chair);
        if (seatIndex === -1) {
            const seatNumberElement = document.querySelector("#seat-" + seat.chair + ">span");
            const seatElemenet = document.getElementById("seat-" + seat.chair);

            seatElemenet.classList.remove("bg-warning");
            seatNumberElement.classList.remove("text-dark");
        }
    }, [selectedSeats, seat.chair])



    const handleClick = (e) => {
        e.preventDefault();
        const seatElemenet = document.getElementById("seat-" + seat.chair);
        const seatNumberElement = document.querySelector("#seat-" + seat.chair + ">span");
        if (seatElemenet.classList.contains("bg-warning")) {
            seatElemenet.classList.remove("bg-warning");
            seatNumberElement.classList.remove("text-dark");
            removeFromSelected(seat);

        } else {
            seatElemenet.classList.add("bg-warning");
            seatNumberElement.classList.add("text-dark");
            addToSelected(seat);
        }
    }

    return (
        <div className="col d-flex justify-content-center">
            <div id={"seat-" + seat.chair} onClick={handleClick} className={bgColor + " text-center d-flex align-items-center justify-content-center"} style={{ width: "3rem", height: "3.5rem", border: "5px solid black", cursor: `${cursor}` }}>
                <span>{seat.chair}</span>
            </div>
        </div>
    )
}

export default Seat;
