import * as Data from "./data.js";
import * as Utils from "./utils.js";
import * as Api from "./apiService.js";

let currentIndex = 0;
let galleryImages = [];

function renderGallery(images) {
    galleryImages = images;
    currentIndex = 0;

    const mainImage = document.getElementById("main-image");
    const thumbnails = document.getElementById("thumbnails");

    if (!mainImage || !thumbnails) return;

    mainImage.src = images[0];
    thumbnails.innerHTML = "";

    images.forEach((img, index) => {
        const thumb = document.createElement("img");
        thumb.src = img;
        thumb.className =
            "w-full h-full object-cover aspect-square bg-[#F3F4F6] rounded-xl overflow-hidden cursor-pointer border-2 transition " +
            (index === 0 ? "border-[#F54900]" : "border-transparent");

        thumb.addEventListener("click", () => {
            currentIndex = index;
            updateGallery();
        });

        thumbnails.appendChild(thumb);
    });

    document.getElementById("prev-btn").onclick = () => {
        currentIndex = (currentIndex - 1 + images.length) % images.length;
        updateGallery();
    };

    document.getElementById("next-btn").onclick = () => {
        currentIndex = (currentIndex + 1) % images.length;
        updateGallery();
    };
}

function updateGallery() {
    const mainImage = document.getElementById("main-image");
    const thumbs = document.getElementById("thumbnails")?.children;

    if (!mainImage || !thumbs) return;

    mainImage.src = galleryImages[currentIndex];

    for (let i = 0; i < thumbs.length; i++) {
        thumbs[i].classList.toggle("border-[#F54900]", i === currentIndex);
        thumbs[i].classList.toggle("border-transparent", i !== currentIndex);
    }
}


function initSpecsAccordion() {
    const trigger = document.getElementById("trigger");
    const content = document.getElementById("content");
    const chevron = document.getElementById("chevron");
    const accordion = document.getElementById("accordion");

    if (!trigger) return;

    trigger.addEventListener("click", () => {
        const isOpen = accordion.getAttribute("data-state") === "open";

        if (isOpen) {
            accordion.setAttribute("data-state", "closed");
            content.style.maxHeight = "0px";
            chevron.classList.remove("rotate-180");
        } else {
            accordion.setAttribute("data-state", "open");
            content.style.maxHeight = content.scrollHeight + "px";
            chevron.classList.add("rotate-180");
        }
    });

    if (accordion.getAttribute("data-state") === "open") {
        content.style.maxHeight = content.scrollHeight + "px";
    } else {
        content.style.maxHeight = "0px";
    }
}

async function renderProduct(product) {
    document.getElementById("product-name").textContent = product.name;
    document.getElementById("nav-name").textContent = product.name;
    document.getElementById("product-price").textContent = `$${product.price.toFixed(2)}`;
    document.getElementById("product-description").textContent = product.description;

    const images = [product.image, product.image, product.image];
    renderGallery(images);

    document.getElementById("product-rating-stars").innerHTML =
        Utils.renderRatingStars(product.rating, 24);
    document.getElementById("product-rating-text").textContent =
        `(${product.rating.toFixed(1)})`;

    const specsGrid = document.getElementById("grid");
    specsGrid.innerHTML = "";

    Object.entries(product.technicalSpecifications).forEach(([key, value]) => {
        specsGrid.innerHTML += `
            <div class="flex flex-col space-y-1 pb-4 border-b border-gray-200">
                <dt class="text-sm text-[#4A5565] uppercase tracking-wide">${key}</dt>
                <dd class="text-base font-semibold text-[#101828]">${value}</dd>
            </div>`;
    });

    initSpecsAccordion();

    const relatedIds = product.relatedProducts || [];
    const relatedSection = document.getElementById("related-section");
    const relatedContainer = document.getElementById("related-products");

    if (relatedIds.length === 0) {
        relatedSection.style.display = "none";
        return;
    }

    relatedSection.style.display = "block";
    relatedContainer.innerHTML = "";

    for (const id of relatedIds) {
        const related = await getById(id);
        if (!related) return;

        const card = document.createElement("a");
        card.href = `product.html?id=${related.id}`;
        card.className = "group bg-white border border-gray-200 rounded-lg overflow-hidden hover:shadow-lg transition block";

        card.innerHTML = `
             <div class="group bg-white border border-[#E5E7EB] rounded-lg overflow-hidden hover:shadow-lg transition">
                    <a href="product.html?id=${related.id}">
                        <div class="aspect-square overflow-hidden bg-gray-100">
                            <img src="${related.image}" class="w-full h-full object-cover group-hover:scale-105 transition">
                        </div>
                    </a>

                    <div class="p-4 flex flex-col gap-2">
                        <a href="product.html?id=${related.id}">
                            <h3 class="text-[#101828] font-semibold text-lg group-hover:text-[#F54900] transition">${related.name}</h3>
                        </a>

                        <div class="flex items-center gap-0.5">
                            ${Utils.renderRatingStars(related.rating, 16)}
                            <span class="ml-2 text-sm text-[#4A5565]">(${related.rating.toFixed(1)})</span>
                        </div>

                        <p class="text-xl font-bold text-[#101828]">$${related.price.toFixed(2)}</p>
                    </div>
        `;

        relatedContainer.appendChild(card);
    }
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

document.getElementById("reduce-quantity-button").addEventListener("click", function(){
    const quantityInput = document.getElementById("quantity-input");
    quantityInput.value = parseInt(quantityInput.value)-1;
});

document.getElementById("add-quantity-button").addEventListener("click", function(){
    const quantityInput = document.getElementById("quantity-input");
    quantityInput.value = parseInt(quantityInput.value)+1;
});

document.getElementById("add-to-cart-button").addEventListener("click", function(){
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    const quantity = parseInt(document.getElementById("quantity-input").value);
    const productName = document.getElementById("product-name").textContent;
    addToCart(productId, quantity);
    Utils.showNotification("Added " + productName + " to cart");
    updateCartBadge();
});

document.getElementById("buy-now-button").addEventListener("click", function(){
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    const quantity = parseInt(document.getElementById("quantity-input").value);
    const productName = document.getElementById("product-name").textContent;
    addToCart(productId, quantity);
    Utils.showNotification("Added " + productName + " to cart");
    window.location.href = "/cart.html";
});

function addToCart(productId, quantity){
    let cart = JSON.parse(localStorage.getItem("cart")) || {};
    if (cart[productId]) {
        cart[productId] = cart[productId] + quantity;
    }
    else {
        cart[productId] = quantity;
    }
    localStorage.setItem("cart", JSON.stringify(cart));
}

async function getById(id){
    const DATA = await Api.getById(id);
    return DATA;
}

async function initProductPage() {
    const id = Utils.getProductIdFromUrl();
    const product = await getById(id);
    if (!product) {
        document.getElementById("product-page").innerHTML = `
            <div class="text-center">
                <h1 class="mb-4">Product Not Found</h1>
                <a class="text-[#F54900] hover:text-[#b53600]" href="index.html" data-discover="true">
                    Return to Catalog
                </a>
            </div>`;
        return;
    }
    await renderProduct(product);
    updateCartBadge();
    Utils.initMobileHeader();
}

initProductPage();