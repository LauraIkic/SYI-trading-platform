package at.ikic.tradingPlatform.controller;

import at.ikic.tradingPlatform.dto.request.AuthRequestDto;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.mapper.AuthResponseMapper;
import at.ikic.tradingPlatform.repository.UserRepository;
import at.ikic.tradingPlatform.dto.response.AuthResponseDto;
import at.ikic.tradingPlatform.service.AuthService;
import at.ikic.tradingPlatform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  AuthService authService;
    @Autowired
    private  AuthResponseMapper authResponseMapper;

    @PostMapping("/signup")
    public AuthResponseDto register (@RequestBody User user) throws Exception {
        if (null != userRepository.findByMail(user.getMail())) {
            throw new Exception("This mail is already in use.");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        authService.authenticateNewUser(user);

        return authResponseMapper.toAuthResponseDto(JwtTokenProvider.generateToken(), "User registered successfully.");
    }

    @PostMapping("/login")
    public AuthResponseDto login (@RequestBody AuthRequestDto data) {
        Authentication auth = authService.authenticateUser(data);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return authResponseMapper.toAuthResponseDto(JwtTokenProvider.generateToken(), "User logged in successfully.");
    }
}
