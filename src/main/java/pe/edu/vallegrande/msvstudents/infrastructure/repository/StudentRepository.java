package pe.edu.vallegrande.msvstudents.infrastructure.repository;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository {
    Flux<Student> findAll();
    Mono<Student> findById(String id);
    Mono<Student> save(Student student);
    Mono<Void> deleteById(String id);
    Mono<Student> findByDocumentNumber(String documentNumber);
    Flux<Student> findByStatus(Status status);
    Flux<Student> findByGender(Gender gender);
    Flux<Student> findByFirstNameContainingIgnoreCase(String firstName);
    Flux<Student> findByLastNameContainingIgnoreCase(String lastName);
    Flux<Student> findAllByOrderByCreatedAtAsc();
    Mono<Long> countByStatus(Status status);
} 