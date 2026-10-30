export const url = "http://localhost:8080/api";

export async function getAll(page, size, sort, search = null) {
    const params = new URLSearchParams();

    if (page !== null && page !== undefined) {
        params.append("page", page);
    }

    if (size !== null && size !== undefined) {
        params.append("limit", size);
    }

    if (sort !== null && sort !== undefined) {
        const [sortBy, order] = sort.split("_");
        params.append("sort_by", sortBy === "name" ? "title" : sortBy);
        params.append("order", order);
    }

    if (search !== null && search !== undefined && search.trim() !== "") {
        params.append("search", search);
    }

    try {
        const response = await fetch(`${url}/products/?${params.toString()}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getById(id) {
    try {
        const response = await fetch(`${url}/products/id/${id}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getByDiscount(page = 0, size = 6, minDiscount = 0, sort = "name_asc") {
    const params = new URLSearchParams();

    params.append("page", page);
    params.append("limit", size);
    params.append("min_discount", minDiscount);

    if (sort !== null && sort !== undefined) {
        const [sortBy, order] = sort.split("_");
        params.append("sort_by", sortBy === "name" ? "title" : sortBy);
        params.append("order", order);
    }

    try {
        const response = await fetch(`${url}/products/discount?${params.toString()}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getByParams(minPrice, maxPrice, rating, category, page, size, sort, search = null) {
    const params = new URLSearchParams();

    if (page !== null && page !== undefined) {
        params.append("page", page);
    }

    if (size !== null && size !== undefined) {
        params.append("limit", size);
    }

    if (sort !== null && sort !== undefined) {
        const [sortBy, order] = sort.split("_");
        params.append("sort_by", sortBy === "name" ? "title" : sortBy);
        params.append("order", order);
    }

    if (category !== null && category !== undefined && category.trim() !== "") {
        params.append("category", category);
    }

    if (rating !== null && rating !== undefined) {
        params.append("min_rating", rating);
    }

    if (minPrice !== null && minPrice !== undefined) {
        params.append("min_price", minPrice);
    }

    if (maxPrice !== null && maxPrice !== undefined) {
        params.append("max_price", maxPrice);
    }

    if (search !== null && search !== undefined && search.trim() !== "") {
        params.append("search", search);
    }

    try {
        const response = await fetch(`${url}/products/?${params.toString()}`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function register(email, password) {
    try {
        const response = await fetch(`${url}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(
                errorData.error ||
                errorData.message ||
                `HTTP ${response.status}`
            );
        }

        return true;

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function login(email, password) {
    try {
        const response = await fetch(`${url}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(
                errorData.error ||
                errorData.message ||
                `HTTP ${response.status}`
            );
        }

        return await response.json();

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getCart(token) {
    try {
        const response = await fetch(`${url}/cart/`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function addToCart(productId, quantity, token) {
    try {
        const response = await fetch(`${url}/cart/items`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                productId: Number(productId),
                quantity: quantity
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function updateCartItem(productId, quantity, token) {
    try {
        const response = await fetch(`${url}/cart/items/${productId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                quantity: quantity
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function deleteCartItem(productId, token) {
    try {
        const response = await fetch(`${url}/cart/items/${productId}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function applyPromoCode(promoCode, token) {
    try {
        const response = await fetch(`${url}/cart/apply-promo`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                promoCode: promoCode
            })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));
            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getAdminProducts(page, limit, search, token) {
    try {
        const params = new URLSearchParams({
            page: page,
            limit: limit,
            sort_by: "title",
            order: "asc"
        });

        if (search) {
            params.append("search", search);
        }

        const response = await fetch(`${url}/products/admin/?${params.toString()}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function getAdminProductById(id, token) {
    try {
        const response = await fetch(`${url}/products/admin/id/${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function createProduct(product, token) {
    try {
        const response = await fetch(`${url}/products/admin/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(product)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function patchProduct(id, product, token) {
    try {
        const response = await fetch(`${url}/products/admin/id/${id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(product)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}

export async function deleteProduct(id, token) {
    try {
        const response = await fetch(`${url}/products/admin/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({
                message: response.statusText
            }));

            throw new Error(errorData.error || errorData.message || `HTTP ${response.status}`);
        }

        return true;

    } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
    }
}