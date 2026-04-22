import { auth, provider } from "./firebase-config.js";
import { signInWithPopup, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.0.0/firebase-auth.js";

const currentPage = window.location.pathname;

// Only run sign-in logic on index.html
if (currentPage === "/" || currentPage.includes("index.html")) {
    onAuthStateChanged(auth, (user) => {
        if (user) {
            // Already signed in, go to waiting page
            sessionStorage.setItem("userName", user.displayName);
            sessionStorage.setItem("userEmail", user.email);

            //this is so theres no redirect loop
            if (window.location.pathname.includes("index.html") || window.location.pathname === "/") {
                window.location.href = "src/main/resources/static/start.html";
            }
        } else{
            // Not signed in, show Google popup
            signInWithPopup(auth, provider)
                .then((result) => {
                    sessionStorage.setItem("userName", result.user.displayName);
                    sessionStorage.setItem("userEmail", result.user.email);
                    window.location.href = "src/main/resources/static/start.html";
                })
                .catch((error) => {
                    console.error("Sign in failed:", error.message);
                });
            }
    });
}