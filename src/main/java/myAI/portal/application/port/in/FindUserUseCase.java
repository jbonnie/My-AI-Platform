package myAI.portal.application.port.in;

import myAI.portal.application.domain.entity.User;

import java.util.Optional;

public interface FindUserUseCase {
    Optional<User> findByUsername(String username);
}
