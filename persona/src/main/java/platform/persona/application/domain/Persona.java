package platform.persona.application.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void update(String name, String description, String content) {
        if(name != null && !name.isEmpty()) {
            this.name = name;
        }
        if(description != null && !description.isEmpty()) {
            this.description = description;
        }
        if(content != null && !content.isEmpty()) {
            this.content = content;
        }
    }
}
