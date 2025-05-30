package at.ikic.tradingPlatform.security;

import at.ikic.tradingPlatform.constants.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenProvider {
    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRETE_KEY.getBytes());
    private static final long EXPIRATION_TIME_MS = 86400000L;

    public static String generateToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roles = populateAuthorities(auth.getAuthorities());
        Date now = Date.from(Instant.now());
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("mail", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    public static String getMailFromJwtToke(String token) {
       token = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("mail"));
    }
    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

}
