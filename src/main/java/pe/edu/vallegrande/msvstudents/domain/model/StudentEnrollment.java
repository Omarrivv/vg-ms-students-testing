package pe.edu.vallegrande.msvstudents.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "student_enrollments")
public class StudentEnrollment {
    @Id
    private String id;
    
    @Indexed
    private String studentId; // referencia a Student
    
    @Indexed
    private String classroomId; // referencia a Classroom - contiene Period implícito
    
    @Indexed(unique = true)
    private String enrollmentNumber; // código único de matrícula por período
    
    private LocalDate enrollmentDate;
    private String status; // ACTIVE, COMPLETED, TRANSFERRED, WITHDRAWN, SUSPENDED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
