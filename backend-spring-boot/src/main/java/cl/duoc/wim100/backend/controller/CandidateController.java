package cl.duoc.wim100.backend.controller;

import cl.duoc.wim100.backend.model.CandidateEntity;
import cl.duoc.wim100.backend.repository.CandidateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateRepository repository;

    public CandidateController(CandidateRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CandidateEntity> getAllCandidates() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateEntity> getCandidateById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CandidateEntity> createCandidate(@RequestBody CandidateEntity candidate) {
        CandidateEntity saved = repository.save(candidate);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateEntity> updateCandidate(@PathVariable Long id, @RequestBody CandidateEntity details) {
        return repository.findById(id).map(existing -> {
            existing.setFullName(details.getFullName());
            existing.setEmail(details.getEmail());
            existing.setRut(details.getRut());
            existing.setProfession(details.getProfession());
            existing.setExperienceYears(details.getExperienceYears());
            existing.setStatus(details.getStatus());
            existing.setScore(details.getScore());
            existing.setFeedback(details.getFeedback());
            return ResponseEntity.ok(repository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
