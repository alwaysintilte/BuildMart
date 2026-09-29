import * as Api from "./apiService.js";
import * as Utils from "./utils.js";

let PROMOCODE = null;
const TAX = 8;
let DATA = null;

function renderCartItems(cart) {
    const container = document.getElementById("cart-products");
    container.innerHTML = "";

    cart.products.forEach(product => {
        const quantity = cart.productQuantity[product.id];
        const itemTotal = product.price * quantity;

        container.innerHTML += `
            <div class="p-4 md:p-6" data-id="${product.id}">
                <div class="grid grid-cols-1 md:grid-cols-12 gap-4 items-center">
                    <div class="md:col-span-6 flex gap-4">
                        <a class="flex-shrink-0" href="product.html?id=${product.id}">
                            <img src="${product.image}" alt="${product.name}" class="w-20 h-20 md:w-24 md:h-24 object-cover rounded-lg">
                        </a>
                        <div class="flex-1">
                            <a class="font-semibold text-[#101828] hover:text-[#F54900] transition" href="product.html?id=${product.id}">
                                ${product.name}
                            </a>
                            <p class="text-sm text-[#4A5565] mt-1">${product.category}</p>
                            <button class="md:hidden flex items-center gap-1 text-sm text-red-700 hover:text-red-800 mt-2">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" x2="10" y1="11" y2="17"></line><line x1="14" x2="14" y1="11" y2="17"></line></svg>
                                Remove
                            </button>
                        </div>
                    </div>
                    <div class="md:col-span-2 md:text-center">
                        <span class="md:hidden text-sm text-[#4A5565]">Price:</span>
                        <span class="text-black font-semibold">$${product.price.toFixed(2)}</span>
                    </div>
                    <div class="md:col-span-2 flex justify-start md:justify-center">
                        <div class="flex items-center gap-2 border border-gray-300 rounded-lg">
                            <button data-id="${product.id}" data-name="${product.name}" class="change-quantity-button reduce-quantity-button w-8 h-8 flex items-center justify-center hover:bg-gray-50 transition">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M5 12h14"></path></svg>
                            </button>
                            <span class="product-quantity text-black w-12 text-center font-semibold">${quantity}</span>
                            <button data-id="${product.id}" data-name="${product.name}" class="change-quantity-button add-quantity-button w-8 h-8 flex items-center justify-center hover:bg-gray-50 transition">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4"><path d="M5 12h14"></path><path d="M12 5v14"></path></svg>
                            </button>
                        </div>
                    </div>
                    <div class="md:col-span-2 flex justify-between md:justify-end items-center">
                        <span class="md:hidden text-sm text-[#4A5565]">Total: </span>
                        <span class="text-black font-bold text-lg">$${itemTotal.toFixed(2)}</span>
                        <button data-id="${product.id}" data-name="${product.name}" class="delete-product-button hidden md:block ml-4 text-red-700 hover:text-red-800 transition">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-5"><path d="M3 6h18"></path><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path><line x1="10" x2="10" y1="11" y2="17"></line><line x1="14" x2="14" y1="11" y2="17"></line></svg>
                        </button>
                    </div>
                </div>
            </div>`;
    });
    const addToCartButtons = container.querySelectorAll(".add-quantity-button");
    addToCartButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.getAttribute("data-id");
            const productName = btn.getAttribute("data-name");
            addToCart(productId);
            Utils.showNotification("Added " + productName + " to cart");
            renderCartItems(DATA);
            updatePrices(DATA);
        });
        updateCartBadge();
    });
    const reduceToCartButtons = container.querySelectorAll(".reduce-quantity-button");
    reduceToCartButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.getAttribute("data-id");
            reduceToCart(productId);
            if(checkIfCartNull(DATA.productQuantity)){
                renderCartItems(DATA);
                updatePrices(DATA);
            }
            updateCartBadge();
        });
    });
    const deleteProductButtons = container.querySelectorAll(".delete-product-button");
    deleteProductButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.getAttribute("data-id");
            deleteFromCart(productId);
            if(checkIfCartNull(DATA.productQuantity)){
                renderCartItems(DATA);
                updatePrices(DATA);
            }
            updateCartBadge();
        });
    });
}

function checkIfCartNull(cartData){
    const container = document.getElementById("cart-main-container");
    if (Object.keys(cartData).length === 0) {
        container.innerHTML = `<div class="text-center">
            <h1 class="text-3xl text-black font-bold mb-4">Your Cart is Empty</h1>
            <p class="mb-8">Start shopping to add items to your cart</p>
            <a class="inline-flex items-center gap-2 px-8 py-3 font-medium bg-[#F54900] text-white  hover:bg-[#b53600]  rounded-lg transition" href="index.html">
                Browse Products
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-white size-5">
                    <path d="M5 12h14"/><path d="m12 5 7 7-7 7"/>
                </svg>
            </a>
        </div>`
        return null;
    }
    return cartData;
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

function updatePrices(cart){
    let total = 0;
    const priceValueTotal = calculateTotal(cart);
    document.getElementById("price-value-total").textContent = `$${priceValueTotal.toFixed(2)}`;
    total += priceValueTotal;
    const priceValueTax = priceValueTotal*TAX/100;
    document.getElementById("price-value-tax").textContent = `$${priceValueTax.toFixed(2)}`;
    total += priceValueTax;
    if(PROMOCODE){
        const priceValueDiscount = total*PROMOCODE.value/100;
        document.getElementById("price-value-discount").textContent = `-$${priceValueDiscount.toFixed(2)}`;
        total -= priceValueDiscount;
    }
    const cartPriceTotal = document.getElementById("cart-price-total").textContent = `$${total.toFixed(2)}`;
}

function calculateTotal(cart) {
    let total = 0;
    cart.products.forEach(product => {
        const quantity = cart.productQuantity[product.id];
        const itemTotal = product.price * quantity;
        total += itemTotal;
    });
    return total;
}

function addToCart(productId){
    let cart = JSON.parse(localStorage.getItem("cart")) || {};
    DATA.productQuantity[productId] += 1;
    cart[productId] = DATA.productQuantity[productId];
    localStorage.setItem("cart", JSON.stringify(cart));
}

function reduceToCart(productId){
    let cart = JSON.parse(localStorage.getItem("cart")) || {};
    DATA.productQuantity[productId] -= 1;
    cart[productId] = DATA.productQuantity[productId];
    if (DATA.productQuantity[productId] <= 0) {
        delete DATA.productQuantity[productId];
        delete cart[productId];
        DATA.products = DATA.products.filter(product => (product.id !== productId));
    }
    localStorage.setItem("cart", JSON.stringify(cart));
}

function deleteFromCart(productId){
    let cart = JSON.parse(localStorage.getItem("cart")) || {};
    delete DATA.productQuantity[productId];
    delete cart[productId];
    DATA.products = DATA.products.filter(product => (product.id !== productId));
    localStorage.setItem("cart", JSON.stringify(cart));
}

function applyPromoCode(){
    const promocodeTextInfo = document.getElementById("promocode-text-info");
    promocodeTextInfo.classList.remove("text-grey");
    promocodeTextInfo.classList.add("text-[#F54900]");
    promocodeTextInfo.textContent = `Promo code applied! You saved ${PROMOCODE.value}%`;

    const cartPriceContainer = document.getElementById("cart-price-container");
    const discountDiv = document.createElement("div");
    discountDiv.className = "flex justify-between text-[#F54900]";
    discountDiv.innerHTML = `
        <span>Discount (${PROMOCODE.value}%)</span>
        <span class="price-value" id="price-value-discount"></span>
    `;
    cartPriceContainer.appendChild(discountDiv);
}

document.getElementById("promocode-button").addEventListener("click", async function() {
    const promocode = await getByCode(document.getElementById("promocode-input").value);
    if(!promocode){
        const promocodeTextInfo = document.getElementById("promocode-text-info");
        promocodeTextInfo.textContent = "Something went wrong. Try again";
        return;
    }
    PROMOCODE = promocode;
    applyPromoCode();
    updatePrices(DATA);
});

document.getElementById("checkout-button").addEventListener("click", async function() {
    localStorage.removeItem("cart");
    window.location.href = "/index.html";
});

async function getByCode(code){
    const data = await Api.getByCode(code);
    return data;
}

async function getCart(data){
    DATA = await Api.getCart(data);
}

async function initCartPage(){
    const cartData = checkIfCartNull(JSON.parse(localStorage.getItem("cart")) || {});
    if(cartData){
        await getCart(cartData);
        renderCartItems(DATA);
        updatePrices(DATA);
        updateCartBadge();
    }
    Utils.initMobileHeader();
}

initCartPage();