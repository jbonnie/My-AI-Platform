package myAI.portal.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myAI.portal.application.domain.entity.User;
import myAI.portal.application.port.in.WithdrawUseCase;
import myAI.portal.application.port.out.WithdrawPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawService implements WithdrawUseCase {

    private final WithdrawPort withdrawPort;

    @Override
    public void execute(User user) {
        withdrawPort.execute(user);
    }
}
