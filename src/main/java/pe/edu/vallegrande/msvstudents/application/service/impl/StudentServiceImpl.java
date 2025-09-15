package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    @Override
    public Flux<StudentResponse> findAll() {
        return studentRepository.findAll()
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> findById(String id) {
        return studentRepository.findById(id)
                .map(StudentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with ID: " + id)));
    }

    @Override
    public Mono<StudentResponse> save(CreateStudentRequest request) {
        // Validate document number uniqueness
        return studentRepository.findByDocumentNumber(request.getDocumentNumber())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Document number already exists: " + request.getDocumentNumber()));
                    }
                    Student student = StudentMapper.toEntity(request);
                    return studentRepository.save(student);
                })
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> update(String id, UpdateStudentRequest request) {
        return studentRepository.findById(id)
                .flatMap(existingStudent -> {
                    Student updatedStudent = StudentMapper.updateEntity(existingStudent, request);
                    return studentRepository.save(updatedStudent);
                })
                .map(StudentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with ID: " + id)));
    }

    @Override
    public Mono<Void> delete(String id) {
        return studentRepository.deleteById(id);
    }

    @Override
    public Mono<StudentResponse> findByDocumentNumber(String documentNumber) {
        return studentRepository.findByDocumentNumber(documentNumber)
                .map(StudentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with document number: " + documentNumber)));
    }

    @Override
    public Flux<StudentResponse> findByStatus(Status status) {
        return studentRepository.findByStatus(status)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findByGender(Gender gender) {
        return studentRepository.findByGender(gender)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findByFirstName(String firstName) {
        return studentRepository.findByFirstNameContainingIgnoreCase(firstName)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findByLastName(String lastName) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findAllOrderedByCreatedAt() {
        return studentRepository.findAllByOrderByCreatedAtAsc()
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> restore(String id) {
        return studentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student not found with ID: " + id)))
                .map(student -> {
                    student.setStatus(Status.ACTIVE);
                    student.setUpdatedAt(LocalDateTime.now());
                    return student;
                })
                .flatMap(studentRepository::save)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> saveBulk(List<CreateStudentRequest> requests) {
        return Flux.fromIterable(requests)
                .map(request -> {
                    Student student = new Student();
                    student.setId(UUID.randomUUID().toString());
                    student.setFirstName(request.getFirstName());
                    student.setLastName(request.getLastName());
                    student.setDocumentType(request.getDocumentType());
                    student.setDocumentNumber(request.getDocumentNumber());
                    student.setBirthDate(request.getBirthDate());
                    student.setGender(request.getGender());
                    student.setAddress(request.getAddress());
                    student.setDistrict(request.getDistrict());
                    student.setProvince(request.getProvince());
                    student.setDepartment(request.getDepartment());
                    student.setPhone(request.getPhone());
                    student.setEmail(request.getEmail());
                    student.setGuardianName(request.getGuardianName());
                    student.setGuardianLastName(request.getGuardianLastName());
                    student.setGuardianDocumentType(request.getGuardianDocumentType());
                    student.setGuardianDocumentNumber(request.getGuardianDocumentNumber());
                    student.setGuardianPhone(request.getGuardianPhone());
                    student.setGuardianEmail(request.getGuardianEmail());
                    student.setGuardianRelationship(request.getGuardianRelationship().getValue());
                    student.setStatus(Status.ACTIVE);
                    student.setCreatedAt(LocalDateTime.now());
                    student.setUpdatedAt(LocalDateTime.now());
                    return student;
                })
                .flatMap(studentRepository::save)
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findStudentsNotEnrolled() {
        return studentRepository.findByStatus(Status.ACTIVE)
                .filterWhen(student -> 
                    enrollmentRepository.findByStudentId(student.getId())
                        .filter(enrollment -> !"INACTIVE".equals(enrollment.getStatus()))
                        .collectList()
                        .map(enrollments -> enrollments.isEmpty())
                )
                .map(StudentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> findStudentsNotEnrolledInClassroom(String classroomId) {
        return studentRepository.findByStatus(Status.ACTIVE)
                .filterWhen(student -> 
                    enrollmentRepository.findByStudentId(student.getId())
                        .filter(enrollment -> classroomId.equals(enrollment.getClassroomId()) && !"INACTIVE".equals(enrollment.getStatus()))
                        .collectList()
                        .map(enrollments -> enrollments.isEmpty())
                )
                .map(StudentMapper::toResponse);
    }

    @Override
    public Mono<Object> getEnrollmentStats() {
        return Mono.zip(
            studentRepository.countByStatus(Status.ACTIVE),
            enrollmentRepository.countByStatus("ACTIVE"),
            enrollmentRepository.countByStatus("COMPLETED"),
            enrollmentRepository.countByStatus("TRANSFERRED"),
            enrollmentRepository.countByStatus("WITHDRAWN"),
            enrollmentRepository.countByStatus("SUSPENDED"),
            enrollmentRepository.countByStatus("INACTIVE"),
            // Contar estudiantes únicos con cualquier matrícula activa (no INACTIVE)
            enrollmentRepository.findAll()
                .filter(enrollment -> !"INACTIVE".equals(enrollment.getStatus()))
                .map(enrollment -> enrollment.getStudentId())
                .distinct()
                .count()
        ).map(tuple -> {
            Map<String, Object> stats = new HashMap<>();
            Long totalActiveStudents = tuple.getT1();
            Long totalActiveEnrollments = tuple.getT2();
            Long uniqueStudentsWithActiveEnrollments = tuple.getT8();
            
            stats.put("totalActiveStudents", totalActiveStudents);
            stats.put("totalActiveEnrollments", totalActiveEnrollments);
            stats.put("totalCompletedEnrollments", tuple.getT3());
            stats.put("totalTransferredEnrollments", tuple.getT4());
            stats.put("totalWithdrawnEnrollments", tuple.getT5());
            stats.put("totalSuspendedEnrollments", tuple.getT6());
            stats.put("totalInactiveEnrollments", tuple.getT7());
            stats.put("uniqueStudentsEnrolled", uniqueStudentsWithActiveEnrollments);
            stats.put("studentsNotEnrolled", totalActiveStudents - uniqueStudentsWithActiveEnrollments);
            stats.put("enrollmentRate", totalActiveStudents > 0 ? (double) uniqueStudentsWithActiveEnrollments / totalActiveStudents * 100 : 0.0);
            return stats;
        });
    }
} 