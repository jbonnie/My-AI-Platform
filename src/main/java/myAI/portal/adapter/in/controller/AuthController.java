package myAI.portal.adapter.in.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myAI.portal.adapter.in.dto.LoginRequestDto;
import myAI.portal.adapter.in.dto.SignupRequestDto;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.FindUserUseCase;
import myAI.portal.application.port.in.SignupUseCase;
import myAI.portal.application.port.in.WithdrawUseCase;
import myAI.portal.infrastructure.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final FindUserUseCase findUserUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SignupUseCase signupUseCase;
    private final WithdrawUseCase withdrawUseCase;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenValidityInMilliseconds;

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletResponse response, @RequestBody LoginRequestDto requestDto) {
        User user = findUserUseCase.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 사용자입니다. 회원가입해주세요."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        // Access Token과 Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        // 쿠키에 토큰 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // 개발: false, 프로덕션: true
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int)(accessTokenValidityInMilliseconds/1000));

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int)(refreshTokenValidityInMilliseconds/1000));

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().body("환영합니다, " + user.getUsername() + "님!");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        // 아이디 중복 확인
        Optional<User> alreadyExistUser = findUserUseCase.findByUsername(requestDto.getUsername());
        if(alreadyExistUser.isPresent()) {
            return ResponseEntity.badRequest().body("중복되는 아이디입니다. 다른 아이디로 시도해주세요.");
        }
        signupUseCase.execute(requestDto.toEntity());
        return ResponseEntity.ok("회원가입이 완료되었습니다. 다시 로그인해주세요.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(HttpServletResponse response,
                                           Authentication authentication) {

        // 현재 로그인한 사용자 정보
        String username = authentication.getName();
        User user = findUserUseCase.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 사용자입니다."));

        // 사용자 삭제
        withdrawUseCase.execute(user);

        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }
}
