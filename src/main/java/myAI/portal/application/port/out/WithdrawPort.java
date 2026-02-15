package myAI.portal.application.port.out;

import myAI.portal.application.domain.entity.User;

public interface WithdrawPort {
    void execute(User user);
}
