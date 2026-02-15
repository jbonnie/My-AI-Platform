package myAI.portal.application.port.out;

import myAI.portal.application.domain.entity.User;

import java.util.Optional;

public interface FindUserPort {

    Optional<User> findByUsername(String username);
}
