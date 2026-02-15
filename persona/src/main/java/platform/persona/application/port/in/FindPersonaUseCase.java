package platform.persona.application.port.in;

import platform.persona.application.domain.Persona;

import java.util.List;

public interface FindPersonaUseCase {
    List<Persona> findAll();
}
