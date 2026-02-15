package platform.persona.application.port.out;

import platform.persona.application.domain.Persona;

import java.util.List;
import java.util.Optional;

public interface FindPersonaPort {

    List<Persona> findAll();
    Optional<Persona> findById(Long id);
}
