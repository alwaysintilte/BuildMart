import * as Api from "./apiService.js";

const registerForm = document.getElementById("register-form");
const registerButton = document.getElementById("register-button");
const registerError = document.getElementById("register-error");

registerForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    registerError.classList.add("hidden");

    if (password !== confirmPassword) {
        registerError.textContent = "Passwords do not match";
        registerError.classList.remove("hidden");
        return;
    }

    registerButton.disabled = true;
    registerButton.textContent = "Creating account...";

    try {
        await Api.register(email, password);

        window.location.href = "login.html";

    } catch (error) {
        registerError.textContent = error.message;
        registerError.classList.remove("hidden");

    } finally {
        registerButton.disabled = false;
        registerButton.textContent = "Create Account";
    }
});