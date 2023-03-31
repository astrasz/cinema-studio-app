import { useEffect, useState } from "react";

const Seat = ({ seat }) => {

    const [bgColor, setBgColor] = useState("");
    const [cursor, setCursor] = useState("");

    useEffect(() => {
        seat.state === 'SOLD' && setBgColor("text-primary");
        seat.state !== 'SOLD' && setCursor("pointer");
    }, [bgColor, cursor, seat.state])



    const handleClick = (e) => {
        e.preventDefault();
        const seatElemenet = document.getElementById("seat-" + seat.chair);
        if (seatElemenet.classList.contains("bg-primary")) {
            seatElemenet.classList.remove("bg-primary");
        } else {
            seatElemenet.classList.add("bg-primary")
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
