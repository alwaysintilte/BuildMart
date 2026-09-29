import * as Data from "./data.js";
import * as Utils from "./utils.js";
import * as Api from "./apiService.js";

function renderCategoriesPage(products) {
    const container = document.getElementById("categories-grid");
    if (!container) return;

    container.innerHTML = "";

    products.forEach(product => {
        container.innerHTML += `
            <a href="./?category=${encodeURIComponent(product.name)}"
               class="group relative rounded-lg aspect-video overflow-hidden border border-[#E5E7EB] hover:shadow-lg transition-shadow">
                <img src="${product.image}" class="w-full h-full object-cover group-hover:scale-105 transition">
                <div class="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent flex items-end p-6">
                    <div class="text-white">
                        <h3 class="font-semibold text-xl">${product.name}</h3>
                        <p class="text-sm">${product.quantity} product${product.quantity > 1 ? "s" : ""}</p>
                    </div>
                </div>
            </a>`;
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

async function getAllCategories(id){
    const DATA = await Api.getAllCategories();
    return DATA;
}

async function initCategoriesPage() {
    const products = await getAllCategories();
    renderCategoriesPage(products);
    updateCartBadge();
    Utils.initMobileHeader();
}

initCategoriesPage();