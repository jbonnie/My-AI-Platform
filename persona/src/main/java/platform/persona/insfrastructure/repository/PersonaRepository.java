package platform.persona.insfrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import platform.persona.application.domain.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
