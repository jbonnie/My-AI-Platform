package platform.persona.application.port.in;

import platform.persona.application.domain.Persona;

public interface UpdatePersonaUseCase {

    Persona execute(Persona persona);
}
