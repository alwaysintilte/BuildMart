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

    if (images.length === 0) {
        mainImage.src = "";
        thumbnails.innerHTML = "";
        document.getElementById("prev-btn").style.display = "none";
        document.getElementById("next-btn").style.display = "none";
        return;
    }

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

    if (!mainImage || !thumbs || galleryImages.length === 0) return;

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
    document.getElementById("product-name").textContent = product.title;
    document.getElementById("nav-name").textContent = product.title;
    document.getElementById("product-price").textContent = `$${Number(product.price).toFixed(2)}`;
    document.getElementById("product-description").textContent = product.description;

    const images = product.images || [];
    renderGallery(images);

    document.getElementById("product-rating-stars").innerHTML =
        Utils.renderRatingStars(product.rating, 24);

    document.getElementById("product-rating-text").textContent =
        `(${product.rating.toFixed(1)})`;

    const specsGrid = document.getElementById("grid");
    specsGrid.innerHTML = "";

    (product.specifications || []).forEach(specification => {
        specsGrid.innerHTML += `
            <div class="flex flex-col space-y-1 pb-4 border-b border-gray-200">
                <dt class="text-sm text-[#4A5565] uppercase tracking-wide">${specification.name}</dt>
                <dd class="text-base font-semibold text-[#101828]">${specification.value}</dd>
            </div>`;
    });

    initSpecsAccordion();

    // В ProductResponse отсутствует поле relatedProducts.
    // Поэтому связанные товары пока не отображаются.
    document.getElementById("related-section").style.display = "none";
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

document.getElementById("reduce-quantity-button").addEventListener("click", function() {
    const quantityInput = document.getElementById("quantity-input");

    const quantity = parseInt(quantityInput.value);

    if (quantity > 1) {
        quantityInput.value = quantity - 1;
    }
});

document.getElementById("add-quantity-button").addEventListener("click", function() {
    const quantityInput = document.getElementById("quantity-input");

    const quantity = parseInt(quantityInput.value) || 1;

    quantityInput.value = quantity + 1;
});

document.getElementById("add-to-cart-button").addEventListener("click", async function() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");
    const quantity = parseInt(document.getElementById("quantity-input").value);
    const productName = document.getElementById("product-name").textContent;

    try {
        await addToCart(productId, quantity);
        Utils.showNotification("Added " + productName + " to cart");
        await updateCartBadge();
    } catch (error) {
        console.error("Failed to add product:", error);
        Utils.showNotification(error.message);
    }
});

document.getElementById("buy-now-button").addEventListener("click", async function() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");
    const quantity = parseInt(document.getElementById("quantity-input").value);

    try {
        await addToCart(productId, quantity);
        window.location.href = "cart.html";
    } catch (error) {
        console.error("Failed to add product:", error);
        Utils.showNotification(error.message);
    }
});

async function addToCart(productId, quantity) {
    const token = sessionStorage.getItem("token");

    if (!token) {
        throw new Error("Please log in before adding products to cart");
    }

    if (!Number.isInteger(quantity) || quantity < 1) {
        throw new Error("Quantity must be at least 1");
    }

    await Api.addToCart(productId, quantity, token);
}

async function getById(id) {
    const DATA = await Api.getById(id);
    return DATA;
}

async function initProductPage() {
    const id = Utils.getProductIdFromUrl();

    try {
        const product = await getById(id);

        if (!product) {
            document.getElementById("product-page").innerHTML = `
                <div class="text-center">
                    <h1 class="mb-4">Product Not Found</h1>
                    <a class="text-[#F54900] hover:text-[#b53600]" href="index.html">
                        Return to Catalog
                    </a>
                </div>`;
            return;
        }

        await renderProduct(product);
        await updateCartBadge();
        Utils.initMobileHeader();
        Utils.initAuthButtons();

    } catch (error) {
        console.error("Failed to load product:", error);

        document.getElementById("product-page").innerHTML = `
            <div class="text-center">
                <h1 class="mb-4">Failed to load product</h1>
                <p>${error.message}</p>
                <a class="text-[#F54900] hover:text-[#b53600]" href="index.html">
                    Return to Catalog
                </a>
            </div>`;
    }
}

if (!Utils.redirectAdmin()) {
    initProductPage();
}
