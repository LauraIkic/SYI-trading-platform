package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.dto.request.AuthRequestDto;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.mapper.AuthResponseMapper;
import at.ikic.tradingPlatform.repository.UserRepository;
import at.ikic.tradingPlatform.dto.response.AuthResponseDto;
import at.ikic.tradingPlatform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthResponseMapper authResponseMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody User user) {
        try {
            System.out.println("=== REGISTRATION ===");
            System.out.println("Email: " + user.getMail());
            System.out.println("Username: " + user.getUserName());
            
            if (userRepository.findByMail(user.getMail()) != null) {
                return ResponseEntity.badRequest().build();
            }

            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            
            String token = JwtTokenProvider.generateTokenForUser(savedUser);
            
            AuthResponseDto response = authResponseMapper.toAuthResponseDto(token, "Registration successful");
            
            System.out.println("User registered: " + savedUser.getUserName());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto data) {
        try {
            System.out.println("=== LOGIN ===");
            System.out.println("Mail: " + data.getMail());  
            User user = userRepository.findByMail(data.getMail()); 
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(data.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().build();
            }
            
            String token = JwtTokenProvider.generateTokenForUser(user);
            
            AuthResponseDto response = authResponseMapper.toAuthResponseDto(token, "Login successful");
            
            System.out.println("User logged in: " + user.getUserName());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
