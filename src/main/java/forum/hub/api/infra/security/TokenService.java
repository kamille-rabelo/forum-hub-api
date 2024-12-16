package forum.hub.api.infra.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .and()
                .issuer("API ForumHub")
                .subject(user.getUsername())
                .expiration(getExpirationDate())
                .signWith(getSingInKey())
                .issuedAt(new Date())
                .compact();
    }

    private Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + 120 * 60 * 1000);
    }

    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith(getSingInKey())
                .requireIssuer("API ForumHub")
                .build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    private SecretKey getSingInKey() {
        var keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
