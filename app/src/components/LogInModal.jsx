import { useState } from "react";
import { login } from "../api";
import { useAuth } from "../hooks/useAuth";

const LoginModal = () => {

    const { logIn } = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);
    const [isInvalid, setIsInvalid] = useState(true);



    const handleChange = (e) => {
        const { name, value } = e.target;
        let errorsArr = [];
        switch (name) {
            case "email":
                setEmail(value);
                errorsArr = [...errors].filter(error => error !== "email")
                setErrors(errorsArr);
                break;
            case "password":
                setPassword(value);
                errorsArr = [...errors].filter(error => error !== "password" && error !== "repeatedPassword")
                setErrors(errorsArr);
                break;
            default:
                break;
        }

        if (errors.length === 0) {
            setIsInvalid(false);
        }
    }

    const signin = (e) => {
        e.preventDefault();
        setIsInvalid(false);

        const errorsArr = []
        if (!email || (email && !email.includes("@")) || (email && !email.includes("."))) {
            errorsArr.push("email")
        }
        if (!password) {
            errorsArr.push("password");
        }

        if (errorsArr.length !== 0) {
            setIsInvalid(true);
            setErrors(errorsArr);
            return;
        }

        login(JSON.stringify({ email, password }))
            .then(response => response.json())
            .then(data => {
                logIn(data)
            })
            .catch(err => {
                console.log(err.message)
            })
        setErrors([]);
        setEmail("");
        setPassword("");
    }


    return (
        <div className="modal fade" id="loginModal" tabIndex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content bg-dark">
                    <div className="modal-header d-flex justify-content-center pb-0">
                        <h1 className="modal-title fs-5" id="registerModalLabel">Registration</h1>
                    </div>
                    <form onSubmit={signin}>
                        <div className="modal-body">
                            <div className="text-center">
                                {isInvalid && errors.length > 0 && <small className="text-danger">Fix errors, please</small>}
                            </div>
                            <div className="mb-3">
                                <label htmlFor="emailLogin" className="form-label">Email Address</label>
                                <input
                                    onChange={handleChange}
                                    type="email"
                                    className="form-control"
                                    id="emailLogin"
                                    name="email"
                                    value={email}
                                    style={errors.includes("email") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="passwordLogin" className="form-label">Password</label>
                                <input
                                    onChange={handleChange}
                                    type="password"
                                    className="form-control"
                                    id="passwordLogin"
                                    name="password"
                                    value={password}
                                    style={errors.includes("password") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="submit" className="btn btn-outline-primary" data-bs-dismiss={!isInvalid ? "modal" : ""}>Submit</button>
                            <button type="button" className="btn btn-outline-danger" data-bs-dismiss="modal">Dismiss</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default LoginModal;