import Seat from "./Seat"

const Row = ({ seats }) => {

    return (
        <div className="row flex-nowrap mb-2">
            <div className="col-1"><h5 className="text-warning pt-3">{seats[0].row}</h5></div>
            <div className="col-10">
                <div className="row no-gutters">
                    {seats.length !== 0 && seats.map((seat, index) => {
                        return <Seat key={index} seat={seat} />
                    })}
                </div>
            </div>
            <div className="col-1"></div>
        </div>
    )

}

export default Row;