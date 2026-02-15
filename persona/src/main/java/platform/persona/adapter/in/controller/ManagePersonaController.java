package platform.persona.adapter.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import platform.persona.adapter.in.dto.RegisterPersonaRequestDto;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.in.FindPersonaUseCase;
import platform.persona.application.port.in.RegisterPersonaUseCase;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persona")
public class ManagePersonaController {

    private final FindPersonaUseCase findPersonaUseCase;
    private final RegisterPersonaUseCase registerPersonaUseCase;

    // 페르소나 목록 조회
    @GetMapping
    public ResponseEntity<List<Persona>> findAll() {
        List<Persona> personas = findPersonaUseCase.findAll();
        return ResponseEntity.ok().body(personas);
    }

    // 페르소나 등록
    @PostMapping
    public ResponseEntity<Persona> register(@RequestBody RegisterPersonaRequestDto requestDto) {
        Persona saved = registerPersonaUseCase.execute(requestDto.toEntity());
        return ResponseEntity.ok().body(saved);
    }

    // 페르소나 수정

    // 페르소나 삭제
}
