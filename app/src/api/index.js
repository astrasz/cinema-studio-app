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



//tickets