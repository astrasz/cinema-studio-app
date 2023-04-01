const TicketsSummary = ({ selectedSeats, isDiscount, removeFromSelected, toggleDiscount }) => {
    return (
        <>
            <p className="h5 mb-3 mt-4">Selected seats</p>
            <div className="row d-flex justify-content-between align-items-center">
                <table className="mb-5 table table-striped">
                    <thead>
                        <tr className="text-warning">
                            <th scope="col">#</th>
                            <th scope="col">Hall</th>
                            <th scope="col">Row</th>
                            <th scope="col">Chair</th>
                            <th scope="col">Price</th>
                        </tr>
                    </thead>
                    <tbody >
                        {selectedSeats && selectedSeats.length !== 0 && selectedSeats.map((seat, index) => {
                            return (
                                <tr key={index}>
                                    <th scope="row">{index + 1}</th>
                                    <td>{seat.hall}</td>
                                    <td>{seat.row}</td>
                                    <td>{seat.chair}</td>
                                    <td>{isDiscount ? seat.halfPrice : seat.regular}</td>
                                    <td><button className="btn btn-sm btn-outline-danger" onClick={() => removeFromSelected(seat)}>Remove</button></td>
                                </tr>
                            )
                        })}
                    </tbody>
                </table>
                {selectedSeats && selectedSeats.length !== 0 && <p>Total: <span>{selectedSeats.reduce((sum, seat) => {
                    if (isDiscount) {
                        return sum += parseFloat(seat.halfPrice);
                    } else {
                        return sum += parseFloat(seat.regular);
                    }
                }, 0)}</span></p>}
                {selectedSeats && selectedSeats.length !== 0 && (
                    <div className="d-flex flex-column">
                        <div className="align-self-end mb-1 form-check">
                            <input className="form-check-input border-danger" type="checkbox" name="studentCheckbox" onChange={toggleDiscount} />
                            <label className="ms-2 form-check-label" htmlFor="studentCheckbox">I am student</label>
                        </div>
                        <button className="btn btn-outline-danger mt-3">
                            Book selected
                        </button>
                    </div>
                )}
            </div>
        </>
    )
}

export default TicketsSummary;