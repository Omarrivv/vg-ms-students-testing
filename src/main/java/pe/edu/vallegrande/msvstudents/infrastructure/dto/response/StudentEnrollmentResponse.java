package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import lombok.Data;
import lombok.Builder;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StudentEnrollmentResponse {
    private String id;
    private String studentId;
    private String classroomId;
    private String enrollmentNumber;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
