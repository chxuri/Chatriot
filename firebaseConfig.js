
import { getAuth, GoogleAuthProvider, signInWithPopup, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.0.0/firebase-auth.js";
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
const firebaseConfig = {
    apiKey: "AIzaSyBfUodxGm6lv2JA-mGFjjJkJqZzMpFFqoI",
    authDomain: "chatriot-1d7fc.firebaseapp.com",
    databaseURL: "https://chatriot-1d7fc-default-rtdb.firebaseio.com",
    projectId: "chatriot-1d7fc",
    storageBucket: "chatriot-1d7fc.firebasestorage.app",
    messagingSenderId: "255339877075",
    appId: "1:255339877075:web:9465167b6b98580672ba26",
    measurementId: "G-SZ72CF7Q52"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

export { auth, provider };