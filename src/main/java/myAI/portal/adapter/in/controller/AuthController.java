package myAI.portal.adapter.in.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myAI.portal.adapter.in.dto.LoginRequestDto;
import myAI.portal.adapter.in.dto.SignupRequestDto;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.FindUserUseCase;
import myAI.portal.application.port.in.SignupUseCase;
import myAI.portal.application.port.in.WithdrawUseCase;
import myAI.portal.infrastructure.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final FindUserUseCase findUserUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SignupUseCase signupUseCase;
    private final WithdrawUseCase withdrawUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, @RequestBody LoginRequestDto requestDto) {
        User user = findUserUseCase.findByUsername(requestDto.getUsername());

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Access Token과 Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getApiKey());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getApiKey());

        // 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("ACCESS_TOKEN", accessToken);
        session.setAttribute("REFRESH_TOKEN", refreshToken);
        session.setAttribute("API_KEY", user.getApiKey());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        signupUseCase.execute(requestDto.toEntity());
        return ResponseEntity.ok("회원가입이 완료되었습니다. 다시 로그인해주세요.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(HttpServletRequest request,
                                           Authentication authentication) {

        // 현재 로그인한 사용자 정보
        String username = authentication.getName();
        User user = findUserUseCase.findByUsername(username);

        // 사용자 삭제
        withdrawUseCase.execute(user);

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }
}
