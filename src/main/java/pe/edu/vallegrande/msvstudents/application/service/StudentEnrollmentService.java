package pe.edu.vallegrande.msvstudents.application.service;

import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentEnrollmentService {
    Flux<StudentEnrollmentResponse> findAll();
    Mono<StudentEnrollmentResponse> findById(String id);
    Mono<StudentEnrollmentResponse> save(CreateStudentEnrollmentRequest request);
    Mono<StudentEnrollmentResponse> updateStatus(String id, String status);
    Mono<Void> delete(String id);
    Flux<StudentEnrollmentResponse> findByStudentId(String studentId);
    Flux<StudentEnrollmentResponse> findByClassroomId(String classroomId);
    Mono<StudentEnrollmentResponse> findByEnrollmentNumber(String enrollmentNumber);
    Flux<StudentEnrollmentResponse> findByStatus(String status);
    Flux<StudentEnrollmentResponse> findAllOrderedByCreatedAt();
    Flux<StudentEnrollmentResponse> findByStudentIdOrderedByCreatedAt(String studentId);
    Mono<StudentEnrollmentResponse> restore(String id);
    Flux<StudentEnrollmentResponse> saveBulk(List<CreateStudentEnrollmentRequest> requests);
    Mono<Object> getClassroomStats(String classroomId);
    Mono<Object> getEnrollmentDistribution();
}
