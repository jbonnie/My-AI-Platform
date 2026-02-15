package myAI.portal.adapter.out;

import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.out.SignupPort;
import myAI.portal.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignupAdapter implements SignupPort {

    private final UserRepository userRepository;

    @Override
    public User execute(User user) {
        return userRepository.save(user);
    }
}
