package myAI.portal.application.service;

import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.FindUserUseCase;
import myAI.portal.application.port.out.FindUserPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserService implements FindUserUseCase {

    private final FindUserPort findUserPort;

    @Override
    public User findByUsername(String username) {
        return findUserPort.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
