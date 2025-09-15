package pe.edu.vallegrande.msvstudents.infrastructure.repository;

import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentEnrollmentRepository {
    Flux<StudentEnrollment> findAll();
    Mono<StudentEnrollment> findById(String id);
    Mono<StudentEnrollment> save(StudentEnrollment enrollment);
    Mono<Void> deleteById(String id);
    Flux<StudentEnrollment> findByStudentId(String studentId);
    Flux<StudentEnrollment> findByClassroomId(String classroomId);
    Mono<StudentEnrollment> findByEnrollmentNumber(String enrollmentNumber);
    Flux<StudentEnrollment> findByStatus(String status);
    Flux<StudentEnrollment> findAllByOrderByCreatedAtAsc();
    Flux<StudentEnrollment> findByStudentIdOrderByCreatedAtAsc(String studentId);
    Flux<StudentEnrollment> findByStudentIdAndStatus(String studentId, String status);
    Flux<StudentEnrollment> findByStudentIdAndClassroomIdAndStatus(String studentId, String classroomId, String status);
    Mono<Long> countByStatus(String status);
}
