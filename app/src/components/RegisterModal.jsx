import { useState } from "react";
import { register } from "../api";
import { useAuth } from "../hooks/useAuth";

const RegisterModal = () => {

    const { logIn } = useAuth();

    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [errors, setErrors] = useState([]);
    const [isInvalid, setIsInvalid] = useState(true);



    const handleChange = (e) => {
        const { name, value } = e.target;
        let errorsArr = [];
        switch (name) {
            case "firstname":
                setFirstname(value);
                errorsArr = [...errors].filter(error => error !== "firstname")
                setErrors(errorsArr);
                break;
            case "lastname":
                setLastname(value);
                errorsArr = [...errors].filter(error => error !== "lastname")
                setErrors(errorsArr);
                break;
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
            case "repeatedPassword":
                setRepeatedPassword(value);
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

    const createAccount = (e) => {
        e.preventDefault();
        setIsInvalid(false);

        const errorsArr = []
        if (!firstname) {
            errorsArr.push("firstname")
        }
        if (!lastname) {
            errorsArr.push("lastname")
        }
        if (!email || (email && !email.includes("@")) || (email && !email.includes("."))) {
            errorsArr.push("email")
        }
        if (!password || !repeatedPassword || password !== repeatedPassword) {
            errorsArr.push("password");
            errorsArr.push("repeatedPassword")
        }

        if (errorsArr.length !== 0) {
            setIsInvalid(true);
            setErrors(errorsArr);
            return;
        }

        register(JSON.stringify({ firstname, lastname, email, password, repeatedPassword }))
            .then(response => response.json())
            .then(data => {
                logIn(data)
            })
            .catch(err => {
                console.log(err.message)
            })
        setErrors([]);
        setFirstname("");
        setLastname("");
        setEmail("");
        setPassword("");
        setRepeatedPassword("");
    }


    return (
        <div className="modal fade" id="registerModal" tabIndex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content bg-dark">
                    <div className="modal-header d-flex justify-content-center pb-0">
                        <h1 className="modal-title fs-5" id="registerModalLabel">Registration</h1>
                    </div>
                    <form onSubmit={createAccount}>
                        <div className="modal-body">
                            <div className="text-center">
                                {isInvalid && errors.length > 0 && <small className="text-danger">Fix errors, please</small>}
                            </div>

                            <div className="mb-3">
                                <label htmlFor="firstname" className="form-label">First Name</label>
                                <input
                                    onChange={handleChange}
                                    type="text"
                                    className="form-control"
                                    id="firstnam"
                                    name="firstname"
                                    value={firstname}
                                    style={errors.includes("firstname") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="lastname" className="form-label">Last Name</label>
                                <input
                                    onChange={handleChange}
                                    type="text"
                                    className="form-control"
                                    id="lastname"
                                    name="lastname"
                                    value={lastname}
                                    style={errors.includes("lastname") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="email" className="form-label">Email Address</label>
                                <input
                                    onChange={handleChange}
                                    type="email"
                                    className="form-control"
                                    id="email"
                                    name="email"
                                    value={email}
                                    style={errors.includes("email") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="password" className="form-label">Password</label>
                                <input
                                    onChange={handleChange}
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    name="password"
                                    value={password}
                                    style={errors.includes("password") ? { border: "2px solid red" } : {}}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="repeatedPassword" className="form-label">Repeated Password</label>
                                <input
                                    onChange={handleChange}
                                    type="password"
                                    className="form-control"
                                    id="repeatedPassword"
                                    name="repeatedPassword"
                                    value={repeatedPassword}
                                    style={errors.includes("repeatedPassword") ? { border: "2px solid red" } : {}}
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

export default RegisterModal;