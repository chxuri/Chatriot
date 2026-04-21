import { auth, provider } from "./firebase-config.js";
import { signInWithPopup, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.0.0/firebase-auth.js";

// Automatically triggers Google sign-in when page loads
window.onload = () => {
    onAuthStateChanged(auth, (user) => {
        if (user) {
            // User is already signed in
            console.log("Signed in as:", user.displayName);
            onSignedIn(user);
        } else {
            // No user signed in, trigger Google popup automatically
            signInWithPopup(auth, provider)
                .then((result) => {
                    const user = result.user;
                    console.log("Signed in as:", user.displayName);
                    onSignedIn(user);
                })
                .catch((error) => {
                    console.error("Sign in failed:", error.message);
                });
        }
    });
};

function onSignedIn(user) {
    // Store user info so other JS files can use it
    sessionStorage.setItem("userName", user.displayName);
    sessionStorage.setItem("userEmail", user.email);
    sessionStorage.setItem("userPhoto", user.photoURL);

    // Redirect to waiting page after sign in
    window.location.href = "waiting-page.html";
}