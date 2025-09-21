package de.splayfer.roonie;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final String secretKey = System.getenv("JWT_KEY"); // Secret for signing JWTs
    private final long shortExpirationMs = 10800000; // Token validity (3 hour)
    private final long longExpirationMs = 86400000; // Token validity (24 hours)

    public String generateToken(String username, boolean remember) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (remember ? longExpirationMs : shortExpirationMs)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact(); // Creates the token
    }

    public void invalidateToken(String token) {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("authentication");
        try {
            mongoDBDatabase.insert("invalidated-tokens", new Document()
                    .append("token", token)
                    .append("expiresAt", Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateToken(String token) {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("authentication");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // Verifies token integrity
            if (mongoDBDatabase.exists("invalidated-tokens", "token", token))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Extracts the username
    }
}