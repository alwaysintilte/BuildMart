export async function loadProducts() {
    try {
        const response = await fetch('../data.json');
        const data = await response.json();
        return data.products;
    } catch (error) {
        return [];
    }
}
