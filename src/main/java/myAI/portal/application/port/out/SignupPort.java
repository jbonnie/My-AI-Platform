package myAI.portal.application.port.out;

import myAI.portal.application.domain.entity.User;

public interface SignupPort {
    User execute(User user);
}
