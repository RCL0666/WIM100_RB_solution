package cl.duoc.wim100.backend.repository;

import cl.duoc.wim100.backend.model.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {
    Optional<CandidateEntity> findByRut(String rut);
}
