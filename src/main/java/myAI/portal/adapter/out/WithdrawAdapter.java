package myAI.portal.adapter.out;

import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.out.WithdrawPort;
import myAI.portal.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawAdapter implements WithdrawPort {

    private final UserRepository userRepository;

    @Override
    public void execute(User user) {
        userRepository.delete(user);
    }
}
