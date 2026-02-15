package myAI.portal.application.service;

import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.FindApiKeyUseCase;
import myAI.portal.application.port.out.FindUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindApiKeyService implements FindApiKeyUseCase {

    private final FindUserPort findUserPort;

    @Override
    public String byUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = findUserPort.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getApiKey();
    }
}
