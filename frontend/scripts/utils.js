export function getProductIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    return productId;
}


export function renderRatingStars(rating, size = 24)  {
    const full = Math.floor(rating);
    const half = rating % 1 >= 0.5;
    const empty = 5 - full - (half ? 1 : 0);

    let html = "";

    for (let i = 0; i < full; i++) html += `
        <svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="fill-[#FDC700] text-[#FDC700]">
            <path d="M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"/>
        </svg>`;

    if (half) html += `
        <svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class=" fill-[#FDC700] text-[#FDC700]">
            <path d="M12 18.338a2.1 2.1 0 0 0-.987.244L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.12 2.12 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.12 2.12 0 0 0 1.597-1.16l2.309-4.679A.53.53 0 0 1 12 2"/>
        </svg>`;

    for (let i = 0; i < empty; i++) html += `
        <svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class=" text-[#D1D5DC]">
            <path d="M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"/>
        </svg>`;

    return html;
}

export function showNotification(message) {
    const container = document.getElementById("notification-container");
    const notification = document.createElement("div");
    notification.className = "my-notification";
    notification.innerHTML = `
        <svg class="notification-success-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z" clip-rule="evenodd" />
        </svg>
        <div class="notification-message">${message}</div>
    `;
    container.appendChild(notification);
    setTimeout(() => {
        notification.classList.add("notification-fade-out");
        notification.addEventListener("animationend", () => notification.remove());
    }, 3000);
}


export function initMobileHeader() {
    const burgerBtn = document.getElementById("burger-btn");
    const searchBtn = document.getElementById("search-btn");

    const mobileMenu = document.getElementById("mobile-menu");
    const mobileSearch = document.getElementById("mobile-search");


    if (!burgerBtn && !searchBtn) return;

    if (burgerBtn && mobileMenu) {
        burgerBtn.addEventListener("click", () => {
            mobileMenu.classList.toggle("hidden");
            if (mobileSearch) mobileSearch.classList.add("hidden");
        });
    }

    if (searchBtn && mobileSearch) {
        searchBtn.addEventListener("click", () => {
            mobileSearch.classList.toggle("hidden");
            if (mobileMenu) mobileMenu.classList.add("hidden");
        });
    }

    window.addEventListener("resize", () => {
        if (window.innerWidth >= 768) {
            if (mobileMenu) mobileMenu.classList.add("hidden");
            if (mobileSearch) mobileSearch.classList.add("hidden");
        }
    });
}


