import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { getDatabase, ref, set } from "firebase/database";

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

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getDatabase(app);
const provider = new GoogleAuthProvider();

// Function to Login and Store Data
export const loginWithGoogle = async () => {
  try {
    const result = await signInWithPopup(auth, provider);
    const user = result.user;

    // Store user info in Realtime Database
    await set(ref(db, 'users/' + user.uid), {
      username: user.displayName,
      email: user.email,
      profile_picture: user.photoURL
    });

    console.log("User data saved!");
  } catch (error) {
    console.error("Login failed:", error);
  }
};