package platform.persona.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.out.RegisterPersonaPort;
import platform.persona.insfrastructure.repository.PersonaRepository;

@Component
@RequiredArgsConstructor
public class RegisterPersonaAdapter implements RegisterPersonaPort {

    private final PersonaRepository personaRepository;

    @Override
    public Persona execute(Persona persona) {
        return personaRepository.save(persona);
    }
}
