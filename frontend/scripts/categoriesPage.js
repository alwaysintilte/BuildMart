import * as Utils from "./utils.js";
import * as Api from "./apiService.js";

async function updateCartBadge() {
    const badge = document.getElementById("cart-quantity-text");
    const token = sessionStorage.getItem("token");

    if (!badge) return;

    if (!token) {
        badge.style.display = "none";
        return;
    }

    try {
        const cart = await Api.getCart(token);

        let totalQuantity = 0;

        (cart.items || []).forEach(item => {
            totalQuantity += item.quantity;
        });

        badge.textContent = totalQuantity;
        badge.style.display = totalQuantity === 0 ? "none" : "flex";

    } catch (error) {
        console.error("Failed to update cart badge:", error);
        badge.style.display = "none";
    }
}

async function initCategoriesPage() {
    await updateCartBadge();
    Utils.initMobileHeader();
    Utils.initAuthButtons();
}

initCategoriesPage();