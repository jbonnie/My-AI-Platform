package platform.persona.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.in.RegisterPersonaUseCase;
import platform.persona.application.port.out.RegisterPersonaPort;

@Service
@RequiredArgsConstructor
public class RegisterPersonaService implements RegisterPersonaUseCase {

    private final RegisterPersonaPort registerPersonaPort;

    @Override
    public Persona execute(Persona persona) {
        return registerPersonaPort.execute(persona);
    }
}
