package myAI.portal.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenValidityInMilliseconds; // 1시간

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenValidityInMilliseconds; // 7일

    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성
    public String createAccessToken(String username) {
        return createToken(username, accessTokenValidityInMilliseconds);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return createToken(username, refreshTokenValidityInMilliseconds);
    }

    // 토큰 생성 (공통)
    private String createToken(String username, long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(username).build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자 정보 추출
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // 유효하지 않은 토큰
            return false;
        }
    }

    // 만료된 토큰에서도 정보 추출 (Refresh 시 필요)
    public Claims getClaimsFromExpiredToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Request의 Header에서 token 추출
    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
