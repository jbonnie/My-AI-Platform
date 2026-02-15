package myAI.portal.infrastructure.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String accessToken = null;
        String refreshToken = null;

        // 1. 헤더에서 토큰 추출 시도
        accessToken = jwtTokenProvider.getToken(request);

        // 2. 헤더에 없으면 세션에서 가져오기
        if (accessToken == null && session != null) {
            accessToken = (String) session.getAttribute("ACCESS_TOKEN");
            refreshToken = (String) session.getAttribute("REFRESH_TOKEN");
        }

        // 3. Access Token 검증
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                // 유효한 토큰 - 인증 설정
                setAuthentication(accessToken);
            }
            else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                // Access Token 만료, Refresh Token 유효 - 자동 갱신
                Claims claims = jwtTokenProvider.getClaimsFromExpiredToken(accessToken);
                String username = claims.getSubject();

                // 새로운 Access Token 발급
                String newAccessToken = jwtTokenProvider.createAccessToken(username);

                // 세션 업데이트
                if (session != null) {
                    session.setAttribute("ACCESS_TOKEN", newAccessToken);
                }

                // 응답 헤더에 새 토큰 추가 (프론트엔드가 업데이트할 수 있도록)
                response.setHeader("New-Access-Token", newAccessToken);

                // 인증 설정
                setAuthentication(newAccessToken);
            }
            // 둘 다 만료되면 인증 실패 (로그인 필요)
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        String username = jwtTokenProvider.getUsername(token);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
