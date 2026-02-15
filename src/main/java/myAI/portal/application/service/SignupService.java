package myAI.portal.application.service;

import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.SignupUseCase;
import myAI.portal.application.port.out.SignupPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

    private final SignupPort signupPort;

    @Override
    public User execute(User user) {
        return signupPort.execute(user);
    }
}
