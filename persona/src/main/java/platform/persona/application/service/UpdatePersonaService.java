package platform.persona.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.in.UpdatePersonaUseCase;
import platform.persona.application.port.out.FindPersonaPort;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePersonaService implements UpdatePersonaUseCase {

    private final FindPersonaPort findPersonaPort;

    @Override
    public Persona execute(Persona persona) {
        Persona original = findPersonaPort.findById(persona.getId())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 페르소나입니다."));

        original.update(persona.getName(), persona.getDescription(), persona.getContent());
        return original;
    }
}
