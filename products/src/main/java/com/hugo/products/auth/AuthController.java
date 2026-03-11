package com.hugo.products.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final JwtService jwtService;

    private final String USER = "admin";
    private final String PASSWORD = "admin123";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (!USER.equals(request.getUsername()) ||
                !PASSWORD.equals(request.getPassword())) {

            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(request.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }

}