const ModalTip = () => {
    return (
        <div className="modal fade" id="modalTip" tabIndex="-1" aria-labelledby="modalTipLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content bg-dark">
                    <div className="modal-body mt-5">
                        <p className="h4">You need to log in to book a ticket.</p>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-outline-danger" data-bs-dismiss="modal">Close </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ModalTip;