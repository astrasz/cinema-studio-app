const SeatsLegend = () => {
    return (
        <>
            <div className="my-3">
                <p className="h5">Legend</p>
            </div>
            <div className="w-100 mb-3">
                <div className="w-25"><small>Sold</small></div>
                <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow bg-primary" style={{ width: "2rem", height: "2.5rem", border: "5px solid black" }} >
                </div>
            </div>
            <div className="w-100 mb-3">
                <div className="w-25"><small >Free</small></div>
                <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow" style={{ width: "2rem", height: "2.5rem", border: "5px solid black", marginRight: "0" }} >
                </div>
            </div>
            <div className="w-100 mb-3">
                <div className="w-25"><small >Selected</small></div>
                <div className="text-center d-flex align-items-center justify-content-center ms-5 flex-grow bg-warning" style={{ width: "2rem", height: "2.5rem", border: "5px solid black", marginRight: "0" }} >
                </div>
            </div>
        </>
    )
}

export default SeatsLegend;