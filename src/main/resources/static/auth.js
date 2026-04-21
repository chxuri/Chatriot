import { auth, provider } from "./firebase-config.js";
import { signInWithPopup, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.0.0/firebase-auth.js";

window.onload = () => {
    onAuthStateChanged(auth, (user) => {
        if (user) {
            // Already signed in, go straight to waiting page
            onSignedIn(user);
        } else {
            // Not signed in, trigger Google popup
            signInWithPopup(auth, provider)
                .then((result) => {
                    onSignedIn(result.user);
                })
                .catch((error) => {
                    console.error("Sign in failed:", error.message);
                });
        }
    });
};

function onSignedIn(user) {
    fetch("/api/auth/verify", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            uid: user.uid,
            email: user.email,
            displayName: user.displayName
        })
    })
    .then(res => res.json())
    .then(data => {
        sessionStorage.setItem("userName", data.userName);
        sessionStorage.setItem("userEmail", data.email);
        window.location.href = "waiting-page.html";
    });
}