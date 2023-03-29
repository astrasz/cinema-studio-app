import Seat from "./Seat"

const Row = ({ seats }) => {

    return (
        <div className="row flex-nowrap">
            {seats.length !== 0 && seats.map((seat, index) => {
                return <Seat key={index} />
            })}
        </div>
    )

}

export default Row;