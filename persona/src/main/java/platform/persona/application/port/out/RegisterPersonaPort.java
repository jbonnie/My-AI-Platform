package platform.persona.application.port.out;

import platform.persona.application.domain.Persona;

public interface RegisterPersonaPort {
    Persona execute(Persona persona);
}
