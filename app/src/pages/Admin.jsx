import { useState } from "react";
import { addMovie, refreshToken } from "../api";
import { useAuthContext } from "../hooks/useAuthContext";
import { useAuth } from "../hooks/useAuth";

const Admin = () => {


    const { user } = useAuthContext();
    const { refreshAuthorization, logOut } = useAuth();

    const [title, setTitle] = useState("");
    const [minutes, setMinutes] = useState("");
    const [director, setDirector] = useState("");
    const [country, setCountry] = useState("");
    const [premiere, setPremiere] = useState("");
    const [isDisplayed, setIsDisplayed] = useState(true)
    const [errors, setErrors] = useState([]);
    const [isInvalid, setIsInvalid] = useState(true);
    const [response, setResponse] = useState("");


    const handleChange = (e) => {
        const { name, value } = e.target;
        let errorsArr = [];

        switch (name) {
            case "title":
                setTitle(value);
                errorsArr = [...errors].filter(error => error !== "title")
                setErrors(errorsArr);
                break;
            case "minutes":
                setMinutes(value);
                break;
            case "director":
                setDirector(value);
                break;
            case "country":
                setCountry(value);
                break;
            case "premiere":
                setPremiere(value);
                errorsArr = [...errors].filter(error => error !== "premiere")
                setErrors(errorsArr);
                break;
            case "isDisplayed":
                setIsDisplayed(e.target.checked);
                break
            default:
                break;
        }

        if (errors.length === 0) {
            setIsInvalid(false);
        }
    }



    const handleSubmit = (e) => {
        e.preventDefault();
        const errorsArr = []
        if (!title) {
            errorsArr.push("title")
        }
        if (!premiere) {
            errorsArr.push("premiere")
        }


        if (errorsArr.length !== 0) {
            setIsInvalid(true);
            setErrors(errorsArr);
            return;
        }

        const formattedPremiere = premiere + " 00:00 am";

        addMovie(JSON.stringify({ title, minutes, director, country, formattedPremiere, isDisplayed, showTimes: [] }), user.access_token)
            .then(response => response.json())
            .then(data => {
                if (data.expired) {
                    refreshToken(user.refresh_token)
                        .then(response => response.json())
                        .then(data => {
                            if (data.expired) {
                                return logOut();
                            }
                            refreshAuthorization(data);
                            setResponse("Your authorization has been refreshed. Try again");
                        })
                        .catch(err => {
                            throw err
                        })
                }
                setResponse(data)
            })
            .catch(err => {
                console.log(err.message)
            })
    }

    return (
        <div className="container ">
            <div className="add-movie">
                <p className="h4">Add movie</p>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="title" className="form-label">Title</label>
                        <input
                            onChange={handleChange}
                            type="text"
                            className="form-control"
                            id="title"
                            name="title"
                            value={title}
                            style={errors.includes("title") ? { border: "2px solid red" } : {}}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="minutes" className="form-label">Movie lenght in minutes</label>
                        <input
                            onChange={handleChange}
                            type="number"
                            className="form-control"
                            id="minutes"
                            name="minutes"
                            value={minutes} />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="director" className="form-label">Director</label>
                        <input
                            onChange={handleChange}
                            type="text"
                            className="form-control"
                            id="director"
                            name="director"
                            value={director} />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="country" className="form-label">Country</label>
                        <input
                            onChange={handleChange}
                            type="text"
                            className="form-control"
                            id="country"
                            name="country"
                            value={country} />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="premiere" className="form-label">Premiere</label>
                        <input
                            onChange={handleChange}
                            type="date"
                            className="form-control"
                            id="premiere"
                            name="premiere"
                            value={premiere ? premiere : ""}
                            style={errors.includes("premiere") ? { border: "2px solid red" } : {}}

                        />
                    </div>
                    <div className="form-check form-switch mt-4">
                        <label className="form-check-label" htmlFor="isDispayed">Displayed</label>
                        <input
                            onChange={handleChange}
                            className="form-check-input"
                            type="checkbox"
                            id="isDisplayed"
                            name="isDisplayed"
                            checked={isDisplayed}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">Submit</button>
                </form>
            </div>
        </div>
    )
}

export default Admin;