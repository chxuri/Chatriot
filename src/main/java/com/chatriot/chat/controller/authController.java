package com.yourapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Hardcoded users for now — replace with database later
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("student1", "password123");
        users.put("student2", "password456");
        users.put("admin", "adminpass");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Map<String, String> response = new HashMap<>();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            response.put("status", "success");
            response.put("userName", username);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody Map<String, String> body) {
        String uid = body.get("uid");
        String email = body.get("email");
        String displayName = body.get("displayName");

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("userName", displayName);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }
}