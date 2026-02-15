package platform.persona.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import platform.persona.application.port.in.DeletePersonaUseCase;
import platform.persona.application.port.out.DeletePersonaPort;

@Service
@RequiredArgsConstructor
@Transactional
public class DeletePersonaService implements DeletePersonaUseCase {

    private final DeletePersonaPort deletePersonaPort;

    @Override
    public void execute(Long id) {
        deletePersonaPort.execute(id);
    }
}
