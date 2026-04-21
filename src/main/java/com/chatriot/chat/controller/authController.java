@RestController
@RequestMapping("/api/auth")
public class uthController {

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody Map<String, String> body) {
        String uid = body.get("uid");
        String email = body.get("email");
        String displayName = body.get("displayName");

        // You can verify the Firebase token here later if needed
        // For now just send back a confirmation
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("userName", displayName);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }
}