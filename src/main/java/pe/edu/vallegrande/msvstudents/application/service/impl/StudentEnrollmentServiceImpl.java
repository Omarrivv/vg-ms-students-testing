package pe.edu.vallegrande.msvstudents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.util.StudentEnrollmentMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {
    private final StudentEnrollmentRepository enrollmentRepository;

    @Override
    public Flux<StudentEnrollmentResponse> findAll() {
        return enrollmentRepository.findAll()
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> findById(String id) {
        return enrollmentRepository.findById(id)
                .map(StudentEnrollmentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student enrollment not found with ID: " + id)));
    }

    @Override
    public Mono<StudentEnrollmentResponse> save(CreateStudentEnrollmentRequest request) {
        // Validate enrollment number uniqueness
        return enrollmentRepository.findByEnrollmentNumber(request.getEnrollmentNumber())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Enrollment number already exists: " + request.getEnrollmentNumber()));
                    }
                    StudentEnrollment enrollment = StudentEnrollmentMapper.toEntity(request);
                    return enrollmentRepository.save(enrollment);
                })
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> updateStatus(String id, String status) {
        return enrollmentRepository.findById(id)
                .flatMap(existingEnrollment -> {
                    existingEnrollment.setStatus(status);
                    existingEnrollment.setUpdatedAt(LocalDateTime.now());
                    return enrollmentRepository.save(existingEnrollment);
                })
                .map(StudentEnrollmentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student enrollment not found with ID: " + id)));
    }

    @Override
    public Mono<Void> delete(String id) {
        return enrollmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student enrollment not found with ID: " + id)))
                .map(enrollment -> {
                    enrollment.setStatus("INACTIVE");
                    enrollment.setUpdatedAt(LocalDateTime.now());
                    return enrollment;
                })
                .flatMap(enrollmentRepository::save)
                .then();
    }

    @Override
    public Flux<StudentEnrollmentResponse> findByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> findByClassroomId(String classroomId) {
        return enrollmentRepository.findByClassroomId(classroomId)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> findByEnrollmentNumber(String enrollmentNumber) {
        return enrollmentRepository.findByEnrollmentNumber(enrollmentNumber)
                .map(StudentEnrollmentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student enrollment not found with enrollment number: " + enrollmentNumber)));
    }

    @Override
    public Flux<StudentEnrollmentResponse> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> findAllOrderedByCreatedAt() {
        return enrollmentRepository.findAllByOrderByCreatedAtAsc()
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> findByStudentIdOrderedByCreatedAt(String studentId) {
        return enrollmentRepository.findByStudentIdOrderByCreatedAtAsc(studentId)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<StudentEnrollmentResponse> restore(String id) {
        return enrollmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student enrollment not found with ID: " + id)))
                .map(enrollment -> {
                    enrollment.setStatus("ACTIVE");
                    enrollment.setUpdatedAt(LocalDateTime.now());
                    return enrollment;
                })
                .flatMap(enrollmentRepository::save)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Flux<StudentEnrollmentResponse> saveBulk(List<CreateStudentEnrollmentRequest> requests) {
        return Flux.fromIterable(requests)
                .map(request -> {
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
                })
                .flatMap(enrollmentRepository::save)
                .map(StudentEnrollmentMapper::toResponse);
    }

    @Override
    public Mono<Object> getClassroomStats(String classroomId) {
        return Mono.zip(
            enrollmentRepository.findByClassroomId(classroomId).count(),
            enrollmentRepository.findByClassroomId(classroomId)
                .filter(enrollment -> "ACTIVE".equals(enrollment.getStatus())).count(),
            enrollmentRepository.findByClassroomId(classroomId)
                .filter(enrollment -> "COMPLETED".equals(enrollment.getStatus())).count(),
            enrollmentRepository.findByClassroomId(classroomId)
                .filter(enrollment -> "TRANSFERRED".equals(enrollment.getStatus())).count(),
            enrollmentRepository.findByClassroomId(classroomId)
                .filter(enrollment -> "WITHDRAWN".equals(enrollment.getStatus())).count()
        ).map(tuple -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("classroomId", classroomId);
            stats.put("totalEnrollments", tuple.getT1());
            stats.put("activeEnrollments", tuple.getT2());
            stats.put("completedEnrollments", tuple.getT3());
            stats.put("transferredEnrollments", tuple.getT4());
            stats.put("withdrawnEnrollments", tuple.getT5());
            stats.put("completionRate", tuple.getT1() > 0 ? (double) tuple.getT3() / tuple.getT1() * 100 : 0.0);
            return stats;
        });
    }

    @Override
    public Mono<Object> getEnrollmentDistribution() {
        return Mono.zip(
            enrollmentRepository.countByStatus("ACTIVE"),
            enrollmentRepository.countByStatus("COMPLETED"),
            enrollmentRepository.countByStatus("TRANSFERRED"),
            enrollmentRepository.countByStatus("WITHDRAWN"),
            enrollmentRepository.countByStatus("SUSPENDED"),
            enrollmentRepository.countByStatus("INACTIVE")
        ).map(tuple -> {
            Map<String, Object> distribution = new HashMap<>();
            Long total = tuple.getT1() + tuple.getT2() + tuple.getT3() + tuple.getT4() + tuple.getT5() + tuple.getT6();
            distribution.put("totalEnrollments", total);
            distribution.put("active", Map.of("count", tuple.getT1(), "percentage", total > 0 ? (double) tuple.getT1() / total * 100 : 0.0));
            distribution.put("completed", Map.of("count", tuple.getT2(), "percentage", total > 0 ? (double) tuple.getT2() / total * 100 : 0.0));
            distribution.put("transferred", Map.of("count", tuple.getT3(), "percentage", total > 0 ? (double) tuple.getT3() / total * 100 : 0.0));
            distribution.put("withdrawn", Map.of("count", tuple.getT4(), "percentage", total > 0 ? (double) tuple.getT4() / total * 100 : 0.0));
            distribution.put("suspended", Map.of("count", tuple.getT5(), "percentage", total > 0 ? (double) tuple.getT5() / total * 100 : 0.0));
            distribution.put("inactive", Map.of("count", tuple.getT6(), "percentage", total > 0 ? (double) tuple.getT6() / total * 100 : 0.0));
            return distribution;
        });
    }
}
