package platform.persona.adapter.in.dto;

import lombok.Getter;
import platform.persona.application.domain.Persona;

@Getter
public class RegisterPersonaRequestDto {
    private String name;
    private String description;
    private String content;

    public Persona toEntity() {
        return Persona.builder()
                .name(name)
                .description(description)
                .content(content)
                .build();
    }
}
