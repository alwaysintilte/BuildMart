export const url = "http://localhost:8080";

export async function getAll(page, size, sort){
    const params = new URLSearchParams();
    if(page !== null || size !== null){
        params.append("page", page);
        params.append("size", size);
    }
    if(sort !== null){
        params.append("sort", sort);
    }
    try {
        const response = await fetch(`${url}/products/?${params.toString()}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getById(id){
    try {
        const response = await fetch(`${url}/products/id/${id}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getAllCategories(){
    try {
        const response = await fetch(`${url}/products/categories`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getByDiscount(){
    try {
        const response = await fetch(`${url}/products/discount`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getByParams(minPrice, maxPrice, rating, category, page, size, sort){
    const params = new URLSearchParams();
    if(page !== null || size !== null){
        params.append("page", page);
        params.append("size", size);
    }
    if(sort !== null){
        params.append("sort", sort);
    }
    if(category !== null){
        params.append("category", category);
    }
    if(rating !== null){
        params.append("rating", rating);
    }
    if(minPrice !== null || maxPrice !== null) {
        if(minPrice !== null){
            params.append("minPrice", minPrice);
        }
        if(maxPrice !== null){
            params.append("maxPrice", maxPrice);
        }
    }
    try {
        const response = await fetch(`${url}/products/params?${params.toString()}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getByCode(code){
    try {
        const response = await fetch(`${url}/promocodes/code/${code}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}
export async function getCart(data){
    try {
        const response = await fetch('/cart/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error("error");
        }
        return await response.json();
    } catch (error) {
        console.error('API Request Failed:', error);
        throw new Error("error");
    }
}