// auth
// firstname, lastname, email, password, repeatedPassword
export const register = async (payload) => {
    return await fetch("/api/account/register", {
        method: "POST",
        body: payload,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// email, password
export const login = async (payload) => {
    return await fetch("/api/account/login", {
        method: "POST",
        body: payload,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const logout = async () => {
    return await fetch("/api/account/logout", {
        method: "POST",
        body: ""
    })
}

export const refreshToken = async (token) => {
    return await fetch("/api/account/refreshtoken", {
        method: "POST",
        body: "",
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
}


// show times
export const getShowTimes = async () => {
    return await fetch("/api/showTimes", {
        method: 'GET'
    })
}

export const getShowTimeById = async (showTimeId) => {
    return await fetch(`/api/showTimes/${showTimeId}`, {
        method: "GET"
    })
}


//movies

export const addMovie = async (payload, token) => {
    return await fetch("/api/movies", {
        method: "POST",
        body: payload,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
}



//tickets
export const bookManyTickets = async (payload) => {
    return await fetch('/api/tickets/many', {
        method: "POST",
        body: payload
    }
    )
}