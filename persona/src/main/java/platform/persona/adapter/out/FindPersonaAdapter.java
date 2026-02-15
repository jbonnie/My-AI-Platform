package platform.persona.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.out.FindPersonaPort;
import platform.persona.insfrastructure.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindPersonaAdapter implements FindPersonaPort {

    private final PersonaRepository personaRepository;

    @Override
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }
}
