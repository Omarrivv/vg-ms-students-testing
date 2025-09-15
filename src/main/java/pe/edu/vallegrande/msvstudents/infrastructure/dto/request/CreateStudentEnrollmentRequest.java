package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class CreateStudentEnrollmentRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Classroom ID is required")
    private String classroomId;

    @NotBlank(message = "Enrollment number is required")
    private String enrollmentNumber;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;
}
