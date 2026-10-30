import * as Data from "./data.js";
import * as Utils from "./utils.js";
import * as Api from "./apiService.js";

function renderDealsPage(deals) {
    const container = document.getElementById("deals-grid");
    if (!container) return;

    container.innerHTML = "";

    deals.forEach(product => {
        container.innerHTML += `
            <div class="relative">
                <div class="group bg-white border border-[#E5E7EB] rounded-lg overflow-hidden hover:shadow-lg transition">
                    <a href="product.html?id=${product.id}">
                        <div class="aspect-square overflow-hidden bg-gray-100">
                            <img src="${product.images?.[0] || ""}" class="w-full h-full object-cover group-hover:scale-105 transition">
                        </div>
                    </a>

                    <div class="p-4 flex flex-col gap-2">
                        <a href="product.html?id=${product.id}">
                            <h3 class="text-[#101828] font-semibold text-lg group-hover:text-[#F54900] transition">${product.title}</h3>
                        </a>

                        <div class="flex items-center gap-0.5">
                            ${Utils.renderRatingStars(product.rating, 16)}
                            <span class="ml-2 text-sm text-[#4A5565]">(${product.rating.toFixed(1)})</span>
                        </div>

                        <p class="text-2xl font-bold text-[#101828]">$${Number(product.price).toFixed(2)}</p>
                        <p class="text-sm text-[#6A7282]">${product.productCategory}</p>

                        <button data-id="${product.id}" data-name="${product.title}" class="add-to-cart-button flex items-center justify-center gap-2 mt-2 w-full py-2 text-sm font-medium text-white bg-[#F54900] hover:bg-[#b53600] rounded-lg transition">
                            Add To Cart
                        </button>
                    </div>
                </div>
            </div>`;
    });

    const buttons = container.querySelectorAll(".add-to-cart-button");

    buttons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const productId = btn.getAttribute("data-id");
            const productName = btn.getAttribute("data-name");

            btn.disabled = true;

            try {
                await addToCart(productId);
                Utils.showNotification("Added " + productName + " to cart");
                await updateCartBadge();
            } catch (error) {
                console.error("Failed to add product:", error);
                Utils.showNotification(error.message);
            } finally {
                btn.disabled = false;
            }
        });
    });
}

async function updateCartBadge() {
    const badge = document.getElementById("cart-quantity-text");
    const token = sessionStorage.getItem("token");

    if (!token) {
        badge.textContent = "0";
        badge.style.display = "none";
        return;
    }

    try {
        const cart = await Api.getCart(token);

        let totalQuantity = 0;

        if (Array.isArray(cart.items)) {
            cart.items.forEach(item => {
                totalQuantity += Number(item.quantity);
            });
        }

        badge.textContent = totalQuantity;

        if (totalQuantity === 0) {
            badge.style.display = "none";
        } else {
            badge.style.display = "flex";
        }
    } catch (error) {
        console.error("Failed to update cart badge:", error);
        badge.style.display = "none";
    }
}

async function addToCart(productId) {
    const token = sessionStorage.getItem("token");

    if (!token) {
        throw new Error("Please log in before adding products to cart");
    }

    await Api.addToCart(productId, 1, token);
}

async function getByDiscount() {
    const products = await Promise.all([
        Api.getById(1),
        Api.getById(2),
        Api.getById(3),
        Api.getById(4),
    ]);

    return products;
}

async function initDealsPage() {
    try {
        const products = await getByDiscount();
        renderDealsPage(products);
    } catch (error) {
        console.error("Failed to load deals:", error);

        const container = document.getElementById("deals-grid");
        container.textContent = "Failed to load products: " + error.message;
    }

    await updateCartBadge();
    Utils.initMobileHeader();
    Utils.initAuthButtons();
}

if (!Utils.redirectAdmin()) {
    initDealsPage();
}
