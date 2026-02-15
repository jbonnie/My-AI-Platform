package platform.persona.application.port.out;

import platform.persona.application.domain.Persona;

import java.util.List;

public interface FindPersonaPort {

    List<Persona> findAll();
}
