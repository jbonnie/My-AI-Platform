package myAI.portal.application.port.in;

import myAI.portal.application.domain.entity.User;

public interface FindUserUseCase {
    User findByUsername(String username);
}
