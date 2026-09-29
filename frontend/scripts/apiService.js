export const url = "http://localhost:8080";

function emptyPage() {
    return {
        content: [],
        totalElements: 0,
        totalPages: 0,
        size: 0,
        number: 0,
        first: true,
        last: true,
        empty: true
    };
}

export async function getAll(page, size) {
    return emptyPage();
}

export async function getById(id) {
    return null;
}

export async function getAllCategories() {
    return [];
}

export async function getByDiscount() {
    return [];
}

export async function getByParams(minPrice, maxPrice, rating, category, page, size) {
    return emptyPage();
}

export async function getByCode(code) {
    return null;
}

export async function getCart(cartData) {
    return {
        productQuantity: cartData || {},
        products: []
    };
}