import * as Api from "./apiService.js";
import * as Utils from "./utils.js";

let PROMOCODE = null;
const TAX = 8;
let DATA = null;

function renderCartItems(cart) {
    const container = document.getElementById("cart-products");
    container.innerHTML = "";

    cart.items.forEach(item => {
        const product = item.productResponse;
        const quantity = item.quantity;
        const itemTotal = Number(item.price);

        container.innerHTML += `
            <div class="p-4 md:p-6" data-id="${product.id}">
                <div class="grid grid-cols-1 md:grid-cols-12 gap-4 items-center">
                    <div class="md:col-span-6 flex gap-4">
                        <a class="flex-shrink-0" href="product.html?id=${product.id}">
                            <img src="${product.images?.[0] || ""}" alt="${product.title}" class="w-20 h-20 md:w-24 md:h-24 object-cover rounded-lg">
                        </a>
                        <div class="flex-1">
                            <a class="font-semibold text-[#101828] hover:text-[#F54900] transition" href="product.html?id=${product.id}">
                                ${product.title}
                            </a>
                            <p class="text-sm text-[#4A5565] mt-1">${product.productCategory}</p>
                            <button data-id="${product.id}" class="delete-product-button md:hidden flex items-center gap-1 text-sm text-red-700 hover:text-red-800 mt-2">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" x2="10" y1="11" y2="17"></line><line x1="14" x2="14" y1="11" y2="17"></line></svg>
                                Remove
                            </button>
                        </div>
                    </div>
                    <div class="md:col-span-2 md:text-center">
                        <span class="md:hidden text-sm text-[#4A5565]">Price:</span>
                        <span class="text-black font-semibold">$${Number(product.price).toFixed(2)}</span>
                    </div>
                    <div class="md:col-span-2 flex justify-start md:justify-center">
                        <div class="flex items-center gap-2 border border-gray-300 rounded-lg">
                            <button data-id="${product.id}" data-name="${product.title}" class="change-quantity-button reduce-quantity-button w-8 h-8 flex items-center justify-center hover:bg-gray-50 transition">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M5 12h14"></path></svg>
                            </button>
                            <span class="product-quantity text-black w-12 text-center font-semibold">${quantity}</span>
                            <button data-id="${product.id}" data-name="${product.title}" class="change-quantity-button add-quantity-button w-8 h-8 flex items-center justify-center hover:bg-gray-50 transition">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M5 12h14"></path><path d="M12 5v14"></path></svg>
                            </button>
                        </div>
                    </div>
                    <div class="md:col-span-2 flex justify-between md:justify-end items-center">
                        <span class="md:hidden text-sm text-[#4A5565]">Total: </span>
                        <span class="text-black font-bold text-lg">$${itemTotal.toFixed(2)}</span>
                        <button data-id="${product.id}" data-name="${product.title}" class="delete-product-button hidden md:block ml-4 text-red-700 hover:text-red-800 transition">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-5"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" x2="10" y1="11" y2="17"></line><line x1="14" x2="14" y1="11" y2="17"></line></svg>
                        </button>
                    </div>
                </div>
            </div>`;
    });

    const addToCartButtons = container.querySelectorAll(".add-quantity-button");

    addToCartButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const productId = btn.getAttribute("data-id");
            const productName = btn.getAttribute("data-name");

            btn.disabled = true;

            try {
                await addToCart(productId);
                await getCart();
                renderCartItems(DATA);
                updatePrices(DATA);
                updateCartBadge();

                Utils.showNotification("Added " + productName + " to cart");
            } catch (error) {
                Utils.showNotification(error.message);
                btn.disabled = false;
            }
        });
    });

    const reduceToCartButtons = container.querySelectorAll(".reduce-quantity-button");

    reduceToCartButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const productId = btn.getAttribute("data-id");

            btn.disabled = true;

            try {
                await reduceToCart(productId);
                await getCart();

                if (checkIfCartNull(DATA.items)) {
                    renderCartItems(DATA);
                    updatePrices(DATA);
                }

                updateCartBadge();
            } catch (error) {
                Utils.showNotification(error.message);
                btn.disabled = false;
            }
        });
    });

    const deleteProductButtons = container.querySelectorAll(".delete-product-button");

    deleteProductButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const productId = btn.getAttribute("data-id");

            btn.disabled = true;

            try {
                await deleteFromCart(productId);
                await getCart();

                if (checkIfCartNull(DATA.items)) {
                    renderCartItems(DATA);
                    updatePrices(DATA);
                }

                updateCartBadge();
            } catch (error) {
                Utils.showNotification(error.message);
                btn.disabled = false;
            }
        });
    });
}

function checkIfCartNull(cartData) {
    const container = document.getElementById("cart-main-container");

    if (!cartData || cartData.length === 0) {
        container.innerHTML = `<div class="text-center">
            <h1 class="text-3xl text-black font-bold mb-4">Your Cart is Empty</h1>
            <p class="mb-8">Start shopping to add items to your cart</p>
            <a class="inline-flex items-center gap-2 px-8 py-3 font-medium bg-[#F54900] text-white hover:bg-[#b53600] rounded-lg transition" href="index.html">
                Browse Products
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-white size-5">
                    <path d="M5 12h14"/><path d="m12 5 7 7-7 7"/>
                </svg>
            </a>
        </div>`;

        return null;
    }

    return cartData;
}

function updateCartBadge() {
    const badge = document.getElementById("cart-quantity-text");

    if (!badge || !DATA) return;

    const totalQuantity = Number(DATA.totalItems || 0);

    badge.textContent = totalQuantity;

    if (totalQuantity === 0) {
        badge.style.display = "none";
    } else {
        badge.style.display = "flex";
    }
}

function updatePrices(cart) {
    const priceValueTotal = Number(cart.priceResponse.totalPrice);
    const priceValueFinal = Number(cart.priceResponse.finalPrice);

    document.getElementById("price-value-total").textContent = `$${priceValueTotal.toFixed(2)}`;

    const priceValueTax = priceValueTotal * TAX / 100;
    document.getElementById("price-value-tax").textContent = `$${priceValueTax.toFixed(2)}`;

    const priceValueDiscount = priceValueTotal - priceValueFinal;
    const discountElement = document.getElementById("price-value-discount");

    if (discountElement) {
        discountElement.textContent = `-$${priceValueDiscount.toFixed(2)}`;
    }

    const total = priceValueFinal + priceValueTax;

    document.getElementById("cart-price-total").textContent = `$${total.toFixed(2)}`;
}

function calculateTotal(cart) {
    let total = 0;

    cart.items.forEach(item => {
        total += Number(item.price);
    });

    return total;
}

async function addToCart(productId) {
    const token = sessionStorage.getItem("token");

    const item = DATA.items.find(item => item.productResponse.id === Number(productId));

    if (!item) return;

    await Api.updateCartItem(productId, item.quantity + 1, token);
}

async function reduceToCart(productId) {
    const token = sessionStorage.getItem("token");

    const item = DATA.items.find(item => item.productResponse.id === Number(productId));

    if (!item) return;

    if (item.quantity <= 1) {
        await Api.deleteCartItem(productId, token);
    } else {
        await Api.updateCartItem(productId, item.quantity - 1, token);
    }
}

async function deleteFromCart(productId) {
    const token = sessionStorage.getItem("token");

    await Api.deleteCartItem(productId, token);
}

function applyPromoCode() {
    const promocodeTextInfo = document.getElementById("promocode-text-info");

    if (!PROMOCODE) return;

    promocodeTextInfo.classList.remove("text-grey");
    promocodeTextInfo.classList.add("text-[#F54900]");
    promocodeTextInfo.textContent = `Promo code applied! You saved ${PROMOCODE.discountPercentage}%`;

    let discountDiv = document.getElementById("price-value-discount");

    if (!discountDiv) {
        const cartPriceContainer = document.getElementById("cart-price-container");

        const discountContainer = document.createElement("div");
        discountContainer.className = "flex justify-between text-[#F54900]";
        discountContainer.innerHTML = `
            <span>Discount (${PROMOCODE.discountPercentage}%)</span>
            <span class="price-value" id="price-value-discount"></span>
        `;

        cartPriceContainer.appendChild(discountContainer);
    } else {
        discountDiv.previousElementSibling.textContent =
            `Discount (${PROMOCODE.discountPercentage}%)`;
    }
}

document.getElementById("promocode-button").addEventListener("click", async function() {
    const promocodeTextInfo = document.getElementById("promocode-text-info");
    const code = document.getElementById("promocode-input").value.trim();

    if (!code) {
        promocodeTextInfo.textContent = "Enter a promo code";
        return;
    }

    try {
        await getByCode(code);

        await getCart();

        PROMOCODE = DATA.promoCodeResponse;

        applyPromoCode();
        updatePrices(DATA);
        updateCartBadge();
    } catch (error) {
        promocodeTextInfo.textContent = error.message;
    }
});

document.getElementById("checkout-button").addEventListener("click", async function() {
    Utils.showNotification("Checkout is not available yet");
});

async function getByCode(code) {
    const token = sessionStorage.getItem("token");

    const data = await Api.applyPromoCode(code, token);

    return data;
}

async function getCart() {
    const token = sessionStorage.getItem("token");

    if (!token) {
        throw new Error("Please log in to view your cart");
    }

    DATA = await Api.getCart(token);

    return DATA;
}

async function initCartPage() {
    try {
        await getCart();

        if (checkIfCartNull(DATA.items)) {
            renderCartItems(DATA);

            PROMOCODE = DATA.promoCodeResponse;

            if (PROMOCODE) {
                applyPromoCode();
            }

            updatePrices(DATA);
            updateCartBadge();
        }
    } catch (error) {
        console.error("Failed to load cart:", error);

        const container = document.getElementById("cart-main-container");

        container.innerHTML = `
            <div class="text-center">
                <h1 class="text-3xl text-black font-bold mb-4">Unable to load cart</h1>
                <p class="mb-8">${error.message}</p>
                <a href="index.html" class="text-[#F54900] hover:text-[#b53600]">
                    Browse Products
                </a>
            </div>
        `;
    }

    Utils.initMobileHeader();
    Utils.initAuthButtons();
}

if (!Utils.redirectAdmin()) {
    initCartPage();
}