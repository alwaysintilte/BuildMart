import * as Api from "./apiService.js";

const loginForm = document.getElementById("login-form");
const loginButton = document.getElementById("login-button");
const loginError = document.getElementById("login-error");

loginForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    loginError.classList.add("hidden");
    loginButton.disabled = true;
    loginButton.textContent = "Logging in...";

    try {
        const response = await Api.login(email, password);

        if (!response.token) {
            throw new Error("Server did not return an access token");
        }

        sessionStorage.setItem("token", response.token);

        window.location.href = "index.html";

    } catch (error) {
        loginError.textContent = error.message;
        loginError.classList.remove("hidden");

    } finally {
        loginButton.disabled = false;
        loginButton.textContent = "Log In";
    }
});