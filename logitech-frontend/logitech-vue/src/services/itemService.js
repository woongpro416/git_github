import api from "@/api/axios.js";

export const getItems = () => {
    return api.get("/items/list").catch(e => {
        console.log("에러:", e);
        return e.response;
    });
};

export const getItem = (itemId) => {
    return api.get(`/items/detail/${itemId}`).catch(e => e.response); 
};

export const saveItem = (data) => {
    return api.post("/items/new", data).catch(e => e.response)
}

export const updateItem = (itemId, data) => {
    return api.put(`/items/edit/${itemId}`, data).catch(e => e.response)
}

export const deleteItem = (itemId) => {
    return api.delete(`/items/delete/${itemId}`).catch(e => e.response)
}