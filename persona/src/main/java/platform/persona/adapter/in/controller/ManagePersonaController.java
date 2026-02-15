package platform.persona.adapter.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import platform.persona.application.domain.Persona;
import platform.persona.application.port.in.FindPersonaUseCase;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persona")
public class ManagePersonaController {

    private final FindPersonaUseCase findPersonaUseCase;

    // 페르소나 목록 조회
    @GetMapping
    public ResponseEntity<List<Persona>> findAll() {
        List<Persona> personas = findPersonaUseCase.findAll();
        return ResponseEntity.ok().body(personas);
    }

    // 페르소나 등록

    // 페르소나 수정

    // 페르소나 삭제
}
