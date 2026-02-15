package platform.persona.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.in.FindPersonaUseCase;
import platform.persona.application.port.out.FindPersonaPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindPersonaService implements FindPersonaUseCase {

    private final FindPersonaPort findPersonaPort;

    @Override
    public List<Persona> findAll() {
        return findPersonaPort.findAll();
    }
}
