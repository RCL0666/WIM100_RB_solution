package cl.duoc.wim100.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rut;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private Integer experienceYears;
    private String profession;
    private String status;
    private Integer score;
    private String feedback;

    public CandidateEntity() {}

    public CandidateEntity(String rut, String fullName, String email, Integer experienceYears, String profession, String status, Integer score, String feedback) {
        this.rut = rut;
        this.fullName = fullName;
        this.email = email;
        this.experienceYears = experienceYears;
        this.profession = profession;
        this.status = status;
        this.score = score;
        this.feedback = feedback;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
