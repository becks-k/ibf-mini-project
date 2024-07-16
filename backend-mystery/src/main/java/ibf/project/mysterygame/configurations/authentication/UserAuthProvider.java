package ibf.project.mysterygame.configurations.authentication;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import ibf.project.mysterygame.models.authentication.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private final Integer validityPeriod = 24 * 60 * 60 * 1000;

    // create token with username, created date and validity date (24 hrs)
    public String createToken(User user) {
        Date now = new Date();
        Date validityDate = new Date(now.getTime() + validityPeriod); // in ms
        return JWT.create()
            .withIssuer(user.getUsername())
            .withIssuedAt(now)
            .withExpiresAt(validityDate)
            .sign(Algorithm.HMAC256(secretKey));
    }

    
    public Authentication validateToken(String token) {
        DecodedJWT decoded = getVerifier().verify(token);

        User user = User.builder()
            .username(decoded.getIssuer())
            .build();

        // returns authenticated user with credentials (null) and granted authorization (empty)
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public String extractUser(String token) {
        DecodedJWT decoded = getVerifier().verify(token);

        return decoded.getIssuer();
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT decoded = getVerifier().verify(token);
        return decoded.getExpiresAt().before(new Date());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    private JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }


    
}
