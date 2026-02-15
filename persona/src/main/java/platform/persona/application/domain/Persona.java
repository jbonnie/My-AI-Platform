package platform.persona.application.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    public Persona(String name, String description, String content) {
        this.name = name;
        this.description = description;
        this.content = content;
    }
}
