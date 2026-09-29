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
                <div class="absolute z-50 -top-2 -right-2 rounded-full px-3 py-1 bg-red-700 text-white text-xs font-semibold shadow-lg">
                    Save ${product.discount}%
                </div>

                <div class="group bg-white border border-[#E5E7EB] rounded-lg overflow-hidden hover:shadow-lg transition">
                    <a href="product.html?id=${product.id}">
                        <div class="aspect-square overflow-hidden bg-gray-100">
                            <img src="${product.image}" class="w-full h-full object-cover group-hover:scale-105 transition">
                        </div>
                    </a>

                    <div class="p-4 flex flex-col gap-2">
                        <a href="product.html?id=${product.id}">
                            <h3 class="text-[#101828] font-semibold text-lg group-hover:text-[#F54900] transition">${product.name}</h3>
                        </a>

                        <div class="flex items-center gap-0.5">
                            ${Utils.renderRatingStars(product.rating, 16)}
                            <span class="ml-2 text-sm text-[#4A5565]">(${product.rating.toFixed(1)})</span>
                        </div>

                        <p class="text-2xl font-bold text-[#101828]">$${product.price.toFixed(2)}</p>
                        <p class="text-sm text-[#6A7282]">${product.category}</p>

                        <button data-id="${product.id}" data-name="${product.name}" class="add-to-cart-button flex items-center justify-center gap-2 mt-2 w-full py-2 text-sm font-medium text-white bg-[#F54900] hover:bg-[#b53600] rounded-lg transition">
                            Add To Cart
                        </button>
                    </div>
                </div>
            </div>`;
    });
    const buttons = container.querySelectorAll(".add-to-cart-button");
    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.getAttribute("data-id");
            const productName = btn.getAttribute("data-name");
            addToCart(productId);
            Utils.showNotification("Added " + productName + " to cart");
            updateCartBadge();
        });
    });
}

function updateCartBadge() {
    const badge = document.getElementById("cart-quantity-text");
    const cart = JSON.parse(localStorage.getItem("cart")) || {};
    let totalQuantity = 0;
    Object.values(cart).forEach(quantity => {
        totalQuantity += quantity;
    });
    badge.textContent = totalQuantity;
    if (totalQuantity === 0) {
        badge.style.display = "none";
    } else {
        badge.style.display = "flex";
    }
}

function addToCart(productId){
    let cart = JSON.parse(localStorage.getItem("cart")) || {};
    if (cart[productId]) {
        cart[productId] = cart[productId] + 1;
    }
    else {
        cart[productId] = 1;
    }
    localStorage.setItem("cart", JSON.stringify(cart));
}

async function getByDiscount(){
    const DATA = await Api.getByDiscount();
    return DATA;
}

async function initDealsPage() {
    const products = await getByDiscount();
    renderDealsPage(products);
    updateCartBadge();
    Utils.initMobileHeader();
}

initDealsPage();