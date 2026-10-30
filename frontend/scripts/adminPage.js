import * as Api from "./apiService.js";

let DATA = null;
let CURRENT_PAGE = 0;
const CURRENT_SIZE = 6;
let SEARCH = "";

const token = sessionStorage.getItem("token");

const productsSection = document.getElementById("products-section");
const formSection = document.getElementById("product-form-section");
const productForm = document.getElementById("product-form");
const message = document.getElementById("admin-message");

function getRoleFromToken() {
    if (!token) return null;

    try {
        const payload = token.split(".")[1];
        const base64 = payload.replace(/-/g, "+").replace(/_/g, "/");
        const claims = JSON.parse(atob(base64));

        if (claims.exp && claims.exp * 1000 <= Date.now()) {
            return null;
        }

        return claims.role;

    } catch (error) {
        return null;
    }
}

function showMessage(text, isError = false) {
    message.textContent = text;
    message.classList.remove("hidden", "bg-red-50", "text-red-700", "bg-green-50", "text-green-700");

    message.classList.add(isError ? "bg-red-50" : "bg-green-50");
    message.classList.add(isError ? "text-red-700" : "text-green-700");
}

function renderProducts(products) {
    const container = document.getElementById("admin-products");
    container.innerHTML = "";

    if (products.length === 0) {
        container.innerHTML = `
            <tr>
                <td colspan="5" class="p-8 text-center text-[#6A7282]">
                    No products found
                </td>
            </tr>
        `;
        return;
    }

    products.forEach(product => {
        const row = document.createElement("tr");
        row.className = "text-sm";

        row.innerHTML = `
            <td>
                <div class="admin-product-info">
                    <img class="admin-product-image"
                         alt="Product image">

                    <div class="admin-product-details">
                        <p class="admin-product-title"></p>
                        <p class="admin-product-id">ID: ${product.id}</p>
                    </div>
                </div>
            </td>

            <td class="admin-category"></td>

            <td>$${Number(product.price).toFixed(2)}</td>

            <td>${product.stockQuantity}</td>

            <td>
                <div class="admin-actions">
                    <button class="edit-product text-[#F54900]"
                            data-id="${product.id}">
                        Edit
                    </button>

                    <button class="delete-product text-red-600"
                            data-id="${product.id}">
                        Delete
                    </button>
                </div>
            </td>
        `;

        row.querySelector(".admin-product-image").src = product.images?.[0]?.url || "";
        row.querySelector(".admin-product-title").textContent = product.title;
        row.querySelector(".admin-category").textContent = product.productCategory;

        container.appendChild(row);
    });

    container.querySelectorAll(".edit-product").forEach(button => {
        button.addEventListener("click", function() {
            editProduct(this.dataset.id);
        });
    });

    container.querySelectorAll(".delete-product").forEach(button => {
        button.addEventListener("click", function() {
            removeProduct(this.dataset.id);
        });
    });
}

function updatePagination() {
    document.getElementById("admin-page-info").textContent =
        `Page ${CURRENT_PAGE + 1} of ${DATA.totalPages || 1}`;

    document.getElementById("admin-prev").disabled = DATA.first;
    document.getElementById("admin-next").disabled = DATA.last;
}

async function loadProducts() {
    DATA = await Api.getAdminProducts(CURRENT_PAGE, CURRENT_SIZE, SEARCH, token);

    renderProducts(DATA.content);
    updatePagination();
}

function addImage(id = null, url = "") {
    const container = document.getElementById("product-images");

    const row = document.createElement("div");
    row.className = "flex gap-2";

    if (id !== null) row.dataset.id = id;

    row.innerHTML = `
        <input type="url" required placeholder="Image URL"
               class="image-url flex-1 min-w-0 px-4 py-2 border rounded-lg">

        <button type="button" class="remove-image px-4 text-red-600 font-semibold">
            Remove
        </button>
    `;

    row.querySelector(".image-url").value = url;

    row.querySelector(".remove-image").addEventListener("click", function() {
        row.remove();
    });

    container.appendChild(row);
}

function addSpecification(id = null, name = "", value = "") {
    const container = document.getElementById("product-specifications");

    const row = document.createElement("div");
    row.className = "grid grid-cols-1 md:grid-cols-12 gap-2";

    if (id !== null) row.dataset.id = id;

    row.innerHTML = `
        <input type="text" required placeholder="Name"
               class="spec-name md:col-span-5 px-4 py-2 border rounded-lg">

        <input type="text" required placeholder="Value"
               class="spec-value md:col-span-5 px-4 py-2 border rounded-lg">

        <button type="button" class="remove-spec md:col-span-2 text-red-600 font-semibold">
            Remove
        </button>
    `;

    row.querySelector(".spec-name").value = name;
    row.querySelector(".spec-value").value = value;

    row.querySelector(".remove-spec").addEventListener("click", function() {
        row.remove();
    });

    container.appendChild(row);
}

function openProductForm() {
    message.classList.add("hidden");
    productsSection.classList.add("hidden");
    formSection.classList.remove("hidden");

    window.scrollTo({ top: 0, behavior: "smooth" });
}

function closeProductForm() {
    formSection.classList.add("hidden");
    productsSection.classList.remove("hidden");

    productForm.reset();
}

function createNewProduct() {
    productForm.reset();

    document.getElementById("product-id").value = "";
    document.getElementById("product-form-title").textContent = "Add Product";

    document.getElementById("product-images").innerHTML = "";
    document.getElementById("product-specifications").innerHTML = "";

    addImage();

    openProductForm();
}

async function editProduct(id) {
    try {
        const product = await Api.getAdminProductById(id, token);

        productForm.reset();

        document.getElementById("product-id").value = product.id;
        document.getElementById("product-form-title").textContent = "Edit Product";

        document.getElementById("product-title").value = product.title;
        document.getElementById("product-price").value = product.price;
        document.getElementById("product-discount").value = product.discount ?? 0;
        document.getElementById("product-stock").value = product.stockQuantity;
        document.getElementById("product-category").value = product.productCategory;
        document.getElementById("product-description").value = product.description || "";

        document.getElementById("product-images").innerHTML = "";
        document.getElementById("product-specifications").innerHTML = "";

        (product.images || []).forEach(image => {
            addImage(image.id, image.url);
        });

        (product.specifications || []).forEach(specification => {
            addSpecification(specification.id, specification.name, specification.value);
        });

        openProductForm();

    } catch (error) {
        showMessage(error.message, true);
    }
}

async function removeProduct(id) {
    if (!confirm("Are you sure you want to delete this product?")) {
        return;
    }

    try {
        await Api.deleteProduct(id, token);

        await loadProducts();
        showMessage("Product deleted successfully");

    } catch (error) {
        showMessage(error.message, true);
    }
}

document.getElementById("add-product-button").addEventListener("click", createNewProduct);

document.getElementById("cancel-product-button").addEventListener("click", closeProductForm);

document.getElementById("add-image-button").addEventListener("click", function() {
    addImage();
});

document.getElementById("add-specification-button").addEventListener("click", function() {
    addSpecification();
});

productForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const id = document.getElementById("product-id").value;
    const saveButton = document.getElementById("save-product-button");

    const images = [];

    document.querySelectorAll("#product-images > div").forEach(row => {
        const image = {
            url: row.querySelector(".image-url").value.trim()
        };

        if (row.dataset.id) {
            image.id = Number(row.dataset.id);
        }

        images.push(image);
    });

    const specifications = [];

    document.querySelectorAll("#product-specifications > div").forEach(row => {
        const specification = {
            name: row.querySelector(".spec-name").value.trim(),
            value: row.querySelector(".spec-value").value.trim()
        };

        if (row.dataset.id) {
            specification.id = Number(row.dataset.id);
        }

        specifications.push(specification);
    });

    const product = {
        title: document.getElementById("product-title").value.trim(),
        price: Number(document.getElementById("product-price").value),
        discount: Number(document.getElementById("product-discount").value),
        stockQuantity: Number(document.getElementById("product-stock").value),
        productCategory: document.getElementById("product-category").value,
        description: document.getElementById("product-description").value.trim(),
        images: images,
        specifications: specifications
    };

    saveButton.disabled = true;
    saveButton.textContent = "Saving...";

    try {
        if (id) {
            await Api.patchProduct(id, product, token);
        } else {
            await Api.createProduct(product, token);
        }

        closeProductForm();
        CURRENT_PAGE = 0;

        await loadProducts();

        showMessage(id ? "Product updated successfully" : "Product created successfully");

    } catch (error) {
        showMessage(error.message, true);

    } finally {
        saveButton.disabled = false;
        saveButton.textContent = "Save Product";
    }
});

document.getElementById("admin-search").addEventListener("change", async function() {
    SEARCH = this.value.trim();
    CURRENT_PAGE = 0;

    try {
        await loadProducts();
    } catch (error) {
        showMessage(error.message, true);
    }
});

document.getElementById("admin-prev").addEventListener("click", async function() {
    if (CURRENT_PAGE <= 0) return;

    CURRENT_PAGE--;

    try {
        await loadProducts();
    } catch (error) {
        showMessage(error.message, true);
    }
});

document.getElementById("admin-next").addEventListener("click", async function() {
    if (DATA.last) return;

    CURRENT_PAGE++;

    try {
        await loadProducts();
    } catch (error) {
        showMessage(error.message, true);
    }
});

document.getElementById("logout-button").addEventListener("click", function() {
    sessionStorage.removeItem("token");
    window.location.href = "login.html";
});

async function initAdminPage() {
    if (getRoleFromToken() !== "ADMIN") {
        window.location.replace("index.html");
        return;
    }

    try {
        await loadProducts();
    } catch (error) {
        showMessage(error.message, true);
    }
}

initAdminPage();