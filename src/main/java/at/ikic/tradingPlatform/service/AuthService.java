package at.ikic.tradingPlatform.service;

import at.ikic.tradingPlatform.dto.request.AuthRequestDto;
import at.ikic.tradingPlatform.entity.User;
import at.ikic.tradingPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByMail(username);
        if (null == user) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), authorityList);
    }

    public void authenticateNewUser(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getMail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    public Authentication authenticateUser(AuthRequestDto data) {
        UserDetails user = this.loadUserByUsername(data.getMail());
        if (null == user) {
            throw new BadCredentialsException("This mail does not exist.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByMail(authentication.getName());
    }
}
