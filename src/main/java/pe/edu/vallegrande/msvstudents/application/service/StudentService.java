package pe.edu.vallegrande.msvstudents.application.service;

import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentService {
    Flux<StudentResponse> findAll();
    Mono<StudentResponse> findById(String id);
    Mono<StudentResponse> save(CreateStudentRequest request);
    Mono<StudentResponse> update(String id, UpdateStudentRequest request);
    Mono<Void> delete(String id);
    Mono<StudentResponse> findByDocumentNumber(String documentNumber);
    Flux<StudentResponse> findByStatus(Status status);
    Flux<StudentResponse> findByGender(Gender gender);
    Flux<StudentResponse> findByFirstName(String firstName);
    Flux<StudentResponse> findByLastName(String lastName);
    Flux<StudentResponse> findAllOrderedByCreatedAt();
    Mono<StudentResponse> restore(String id);
    Flux<StudentResponse> saveBulk(List<CreateStudentRequest> requests);
    Flux<StudentResponse> findStudentsNotEnrolled();
    Flux<StudentResponse> findStudentsNotEnrolledInClassroom(String classroomId);
    Mono<Object> getEnrollmentStats();
} 