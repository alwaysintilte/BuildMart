import * as Data from "./data.js";
import * as Utils from "./utils.js";
import * as Api from "./apiService.js";

let CATEGORY = null;
let DATA = null;
let PRODUCTS = null;
let CURRENT_SIZE = 4;
let CURRENT_PAGE = 0;

const sortSelector = document.getElementById("sort-selector");
const shownProducts = document.getElementById("shown-products");

const prevBtn = document.getElementById("prev-page");
const nextBtn = document.getElementById("next-page");

const minSlider = document.getElementById("min-price");
const maxSlider = document.getElementById("max-price");
const sliderRange = document.getElementById("slider-range");
const minValueText = document.getElementById("min-value");
const maxValueText = document.getElementById("max-value");
const minValue = 0;
const maxValue = 400;

const buttons = document.querySelectorAll("#rating-container [data-rating]");

function renderProductCards(products) {
    const container = document.getElementById("products-grid");
    if (!container) return;

    container.innerHTML = "";
    shownProducts.textContent = products.length;
    if (products.length === 0) {
        container.innerHTML = `
            <div class="text-center py-12">
                <p class="text-[#6A7282] text-lg">No products match your filters.</p>
                <button class="mt-4 text-[#F54900] hover:text-[#b53600] hover:border-[#b53600] font-medium transition" id="clear_filters_button_no_data">Clear filters</button>
            </div>
        `;
        const clear_filters_button_no_data = document.getElementById("clear_filters_button_no_data");
        clear_filters_button_no_data.addEventListener("click", function() {
            clearFilters();
        });
        return;
    }

    products.forEach(product => {
        container.innerHTML += `
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
                    <button data-id="${product.id}" data-name="${product.name}" class="add-to-cart-button flex items-center justify-center gap-2 mt-2 w-full py-2 text-sm font-medium text-white bg-[#F54900] hover:bg-[#b53600]  rounded-lg transition">
                       <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="size-4 "><circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/>
                             <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                       </svg>
                           Add To Cart
                    </button>
                </div>
            </div>`;
    });
    const buttons = container.querySelectorAll(".add-to-cart-button");
    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            const productId = btn.getAttribute("data-id");
            const productName = btn.getAttribute("data-name");
            addToCart(productId);
            Utils.showNotification("Added: "+productName);
            updateCartBadge();
        });
    });
}

buttons.forEach((btn) => {
    btn.addEventListener("click", async function() {
        const isChecked = btn.getAttribute("data-state") === "checked";
        buttons.forEach((b) => {
            b.setAttribute("data-state", "unchecked");
            b.setAttribute("aria-checked", "false");
            const span = b.querySelector("span[data-state]");
            if (span) span.setAttribute("data-state", "unchecked");
        });
        if (isChecked){
            resetPagination();
            await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), null, CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
            renderProductCards(PRODUCTS);
            updatePagination();
            return;
        }
        btn.setAttribute("data-state", "checked");
        btn.setAttribute("aria-checked", "true");
        const span = btn.querySelector("span[data-state]");
        if (span) span.setAttribute("data-state", "checked");
        resetPagination();
        await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), btn.getAttribute("data-rating"), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
        renderProductCards(PRODUCTS);
        updatePagination();
    });
});

document.getElementById("filters_button").addEventListener("click", function() {
    const filters_div = document.getElementById("filters_div");
    const filters_button_text = document.getElementById("filters_button_text");
    if (filters_div.style.display === "none") {
        filters_button_text.textContent = "Hide Filters";
        filters_div.style.display = "block";
    } else {
        filters_button_text.textContent = "Show Filters";
        filters_div.style.display = "none";
    }
});

document.getElementById("clear_filters_button").addEventListener("click", function() {
    clearFilters();
});

minSlider.addEventListener("input", async function(e) {
    updateSlider(e);
    resetPagination();
    await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), (document.querySelector('[id^="rating-"][data-state="checked"]') ? document.querySelector('[id^="rating-"][data-state="checked"]').getAttribute("data-rating") : null), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    renderProductCards(PRODUCTS);
    updatePagination();
});

maxSlider.addEventListener("input", async function(e) {
    updateSlider(e);
    resetPagination();
    await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), (document.querySelector('[id^="rating-"][data-state="checked"]') ? document.querySelector('[id^="rating-"][data-state="checked"]').getAttribute("data-rating") : null), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    renderProductCards(PRODUCTS);
    updatePagination();
});

function updateSlider(e) {
    let minVal = parseInt(minSlider.value);
    let maxVal = parseInt(maxSlider.value);

    if (e.target.id === "min-price") {
        if (minVal > maxVal) {
            minVal = maxVal;
            minSlider.value = minVal;
        }
    } else if (e.target.id === "max-price") {
        if (minVal > maxVal) {
            maxVal = minVal;
            maxSlider.value = maxVal;
        }
    }

    minValueText.textContent = "$" + minVal;
    maxValueText.textContent = "$" + maxVal;
    sliderRange.style.left = (minVal / maxValue) * 100 + "%";
    sliderRange.style.right = (100 - (maxVal / maxValue) * 100) + "%";
}

function clearFilters(){
    const activeSelected = document.querySelector('[id^="rating-"][data-state="checked"]');
    if(activeSelected){
        activeSelected.setAttribute("data-state", "unchecked");
        activeSelected.setAttribute("aria-checked", "false");
        const activeSelectedSpan = activeSelected.querySelector("span");
        activeSelectedSpan.setAttribute("data-state", "unchecked");
    }

    minSlider.value = minValue;
    maxSlider.value = maxValue;
    minValueText.textContent = "$" + minValue;
    maxValueText.textContent = "$" + maxValue;
    sliderRange.style.left = 0 + "%";
    sliderRange.style.right = 0 + "%";

    initCatalogPage();
}

sortSelector.addEventListener("change", async function() {
    await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), (document.querySelector('[id^="rating-"][data-state="checked"]') ? document.querySelector('[id^="rating-"][data-state="checked"]').getAttribute("data-rating") : null), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    renderProductCards(PRODUCTS);
});

function updatePagination(){
    const pageInfo = document.getElementById("page-info");
    pageInfo.textContent = "Page " + (CURRENT_PAGE + 1);
    prevBtn.disabled = false;
    nextBtn.disabled = false;
    if(DATA.first === true){
        prevBtn.disabled = true;
    }
    if(DATA.last === true){
        nextBtn.disabled = true;
    }
}

function resetPagination(){
    CURRENT_PAGE = 0;
}

prevBtn.addEventListener("click", async function(){
    CURRENT_PAGE = CURRENT_PAGE - 1;
    await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), (document.querySelector('[id^="rating-"][data-state="checked"]') ? document.querySelector('[id^="rating-"][data-state="checked"]').getAttribute("data-rating") : null), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    renderProductCards(PRODUCTS);
    updatePagination();
});

nextBtn.addEventListener("click", async function(){
    CURRENT_PAGE = CURRENT_PAGE + 1;
    await getByParams(parseInt(minSlider.value), parseInt(maxSlider.value), (document.querySelector('[id^="rating-"][data-state="checked"]') ? document.querySelector('[id^="rating-"][data-state="checked"]').getAttribute("data-rating") : null), CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    renderProductCards(PRODUCTS);
    updatePagination();
});

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

async function getByParams(minPrice, maxPrice, rating, category, page, size, sort){
    DATA = await Api.getByParams(minPrice, maxPrice, rating, category, page, size, sort);
    PRODUCTS = DATA.content;
}

async function getAll(page, size, sort){
    DATA = await Api.getAll(page, size, sort);
    PRODUCTS = DATA.content;
}

async function initCatalogPage() {
    const urlParams = new URLSearchParams(window.location.search);
    CATEGORY = urlParams.get("category");
    if(CATEGORY){
        await getByParams(null, null, null, CATEGORY, CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    } else {
        await getAll(CURRENT_PAGE, CURRENT_SIZE, sortSelector.value);
    }
    renderProductCards(PRODUCTS);
    updatePagination();
    updateCartBadge();
    Utils.initMobileHeader();
}

initCatalogPage();