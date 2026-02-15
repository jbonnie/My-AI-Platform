package platform.persona.application.port.in;

import platform.persona.application.domain.Persona;

public interface RegisterPersonaUseCase {
    Persona execute(Persona persona);
}
