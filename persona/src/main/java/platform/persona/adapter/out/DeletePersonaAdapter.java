package platform.persona.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import platform.persona.application.port.out.DeletePersonaPort;
import platform.persona.insfrastructure.repository.PersonaRepository;

@Component
@RequiredArgsConstructor
public class DeletePersonaAdapter implements DeletePersonaPort {

    private final PersonaRepository personaRepository;

    @Override
    public void execute(Long id) {
        personaRepository.deleteById(id);
    }
}
