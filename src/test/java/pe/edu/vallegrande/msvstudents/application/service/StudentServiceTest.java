package pe.edu.vallegrande.msvstudents.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.vallegrande.msvstudents.application.service.impl.StudentServiceImpl;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para StudentService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias del Servicio de Estudiantes")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentEnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student sampleStudent;
    private CreateStudentRequest createRequest;

    @BeforeEach
    void setUp() {
        sampleStudent = createSampleStudent();
        createRequest = createSampleRequest();
    }

    @Test
    @DisplayName("Debe encontrar todos los estudiantes")
    void shouldFindAllStudents() {
        // Given
        when(studentRepository.findAll()).thenReturn(Flux.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findAll())
                .expectNextCount(1)
                .verifyComplete();

        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("Debe encontrar estudiante por ID")
    void shouldFindStudentById() {
        // Given
        String studentId = "test-id";
        when(studentRepository.findById(studentId)).thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findById(studentId))
                .expectNextMatches(response -> response.getId().equals(studentId))
                .verifyComplete();

        verify(studentRepository).findById(studentId);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no encuentra estudiante por ID")
    void shouldThrowExceptionWhenStudentNotFoundById() {
        // Given
        String studentId = "non-existent-id";
        when(studentRepository.findById(studentId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.findById(studentId))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(studentRepository).findById(studentId);
    }

    @Test
    @DisplayName("Debe crear un nuevo estudiante exitosamente")
    void shouldCreateStudentSuccessfully() {
        // Given
        when(studentRepository.findByDocumentNumber(createRequest.getDocumentNumber()))
                .thenReturn(Mono.empty());
        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.save(createRequest))
                .expectNextMatches(response -> 
                    response.getFirstName().equals(createRequest.getFirstName()) &&
                    response.getDocumentNumber().equals(createRequest.getDocumentNumber())
                )
                .verifyComplete();

        verify(studentRepository).findByDocumentNumber(createRequest.getDocumentNumber());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el número de documento ya existe")
    void shouldThrowExceptionWhenDocumentNumberExists() {
        // Given
        when(studentRepository.findByDocumentNumber(createRequest.getDocumentNumber()))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.save(createRequest))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(studentRepository).findByDocumentNumber(createRequest.getDocumentNumber());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe eliminar estudiante por ID")
    void shouldDeleteStudentById() {
        // Given
        String studentId = "test-id";
        when(studentRepository.deleteById(studentId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.delete(studentId))
                .verifyComplete();

        verify(studentRepository).deleteById(studentId);
    }

    @Test
    @DisplayName("Debe encontrar estudiante por número de documento")
    void shouldFindStudentByDocumentNumber() {
        // Given
        String documentNumber = "12345678";
        when(studentRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByDocumentNumber(documentNumber))
                .expectNextMatches(response -> response.getDocumentNumber().equals(documentNumber))
                .verifyComplete();

        verify(studentRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no encuentra estudiante por número de documento")
    void shouldThrowExceptionWhenStudentNotFoundByDocumentNumber() {
        // Given
        String documentNumber = "non-existent";
        when(studentRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(studentService.findByDocumentNumber(documentNumber))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(studentRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Debe restaurar estudiante inactivo")
    void shouldRestoreInactiveStudent() {
        // Given
        String studentId = "test-id";
        sampleStudent.setStatus(Status.INACTIVE);
        
        when(studentRepository.findById(studentId)).thenReturn(Mono.just(sampleStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.restore(studentId))
                .expectNextMatches(response -> response.getStatus() == Status.ACTIVE)
                .verifyComplete();

        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe obtener estadísticas de matrícula")
    void shouldGetEnrollmentStats() {
        // Given
        when(studentRepository.countByStatus(Status.ACTIVE)).thenReturn(Mono.just(100L));
        when(enrollmentRepository.countByStatus("ACTIVE")).thenReturn(Mono.just(80L));
        when(enrollmentRepository.countByStatus("COMPLETED")).thenReturn(Mono.just(20L));
        when(enrollmentRepository.countByStatus("TRANSFERRED")).thenReturn(Mono.just(5L));
        when(enrollmentRepository.countByStatus("WITHDRAWN")).thenReturn(Mono.just(10L));
        when(enrollmentRepository.countByStatus("SUSPENDED")).thenReturn(Mono.just(3L));
        when(enrollmentRepository.countByStatus("INACTIVE")).thenReturn(Mono.just(15L));
        when(enrollmentRepository.findAll()).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(studentService.getEnrollmentStats())
                .expectNextMatches(stats -> stats instanceof java.util.Map)
                .verifyComplete();

        verify(studentRepository).countByStatus(Status.ACTIVE);
        verify(enrollmentRepository, times(7)).countByStatus(anyString());
    }

    @Test
    @DisplayName("Debe encontrar estudiantes ordenados por fecha de creación")
    void shouldFindStudentsOrderedByCreatedAt() {
        // Given
        when(studentRepository.findAllByOrderByCreatedAtAsc())
                .thenReturn(Flux.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findAllOrderedByCreatedAt())
                .expectNextCount(1)
                .verifyComplete();

        verify(studentRepository).findAllByOrderByCreatedAtAsc();
    }

    // Métodos auxiliares

    private Student createSampleStudent() {
        Student student = new Student();
        student.setId("test-id");
        student.setFirstName("Juan");
        student.setLastName("Pérez");
        student.setDocumentType(DocumentType.DNI);
        student.setDocumentNumber("12345678");
        student.setBirthDate(LocalDate.of(2010, 1, 1));
        student.setGender(Gender.MALE);
        student.setAddress("Av. Principal 123");
        student.setDistrict("Lima");
        student.setProvince("Lima");
        student.setDepartment("Lima");
        student.setPhone("987654321");
        student.setEmail("juan@email.com");
        student.setGuardianName("Pedro");
        student.setGuardianLastName("Pérez");
        student.setGuardianDocumentType(DocumentType.DNI);
        student.setGuardianDocumentNumber("87654321");
        student.setGuardianPhone("123456789");
        student.setGuardianEmail("pedro@email.com");
        student.setGuardianRelationship("FATHER");
        student.setStatus(Status.ACTIVE);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    private CreateStudentRequest createSampleRequest() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstName("Juan");
        request.setLastName("Pérez");
        request.setDocumentType(DocumentType.DNI);
        request.setDocumentNumber("12345678");
        request.setBirthDate(LocalDate.of(2010, 1, 1));
        request.setGender(Gender.MALE);
        request.setAddress("Av. Principal 123");
        request.setDistrict("Lima");
        request.setProvince("Lima");
        request.setDepartment("Lima");
        request.setPhone("987654321");
        request.setEmail("juan@email.com");
        request.setGuardianName("Pedro");
        request.setGuardianLastName("Pérez");
        request.setGuardianDocumentType(DocumentType.DNI);
        request.setGuardianDocumentNumber("87654321");
        request.setGuardianPhone("123456789");
        request.setGuardianEmail("pedro@email.com");
        request.setGuardianRelationship(GuardianRelationship.FATHER);
        return request;
    }
}