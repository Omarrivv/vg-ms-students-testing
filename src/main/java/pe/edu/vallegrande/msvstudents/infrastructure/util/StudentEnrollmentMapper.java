package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.domain.enums.EnrollmentStatus;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentEnrollmentMapper {

    public static StudentEnrollment toEntity(CreateStudentEnrollmentRequest request) {
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setStudentId(request.getStudentId());
        enrollment.setClassroomId(request.getClassroomId());
        enrollment.setEnrollmentNumber(request.getEnrollmentNumber());
        enrollment.setEnrollmentDate(request.getEnrollmentDate());
        enrollment.setStatus("ACTIVE");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        return enrollment;
    }

    public static StudentEnrollmentResponse toResponse(StudentEnrollment enrollment) {
        return StudentEnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .classroomId(enrollment.getClassroomId())
                .enrollmentNumber(enrollment.getEnrollmentNumber())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus() != null ? 
                    EnrollmentStatus.valueOf(enrollment.getStatus()) : null)
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
}
