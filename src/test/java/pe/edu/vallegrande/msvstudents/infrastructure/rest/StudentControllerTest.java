package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Pruebas de integración para StudentController
 */
@WebFluxTest(StudentController.class)
@DisplayName("Pruebas del Controlador de Estudiantes")
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentResponse sampleResponse;
    private CreateStudentRequest createRequest;

    @BeforeEach
    void setUp() {
        sampleResponse = createSampleResponse();
        createRequest = createSampleRequest();
    }

    @Test
    @DisplayName("GET /api/students - Debe retornar todos los estudiantes")
    void shouldReturnAllStudents() {
        // Given
        when(studentService.findAll()).thenReturn(Flux.just(sampleResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponse.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe retornar estudiante por ID")
    void shouldReturnStudentById() {
        // Given
        String studentId = "test-id";
        when(studentService.findById(studentId)).thenReturn(Mono.just(sampleResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/students/{id}", studentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StudentResponse.class)
                .value(response -> {
                    assert response.getId().equals(studentId);
                    assert response.getFirstName().equals("Juan");
                });
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe retornar 404 cuando no encuentra estudiante")
    void shouldReturn404WhenStudentNotFound() {
        // Given
        String studentId = "non-existent-id";
        when(studentService.findById(studentId))
                .thenReturn(Mono.error(new ResourceNotFoundException("Student not found")));

        // When & Then
        webTestClient.get()
                .uri("/api/students/{id}", studentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("POST /api/students - Debe crear un nuevo estudiante")
    void shouldCreateNewStudent() {
        // Given
        when(studentService.save(any(CreateStudentRequest.class)))
                .thenReturn(Mono.just(sampleResponse));

        // When & Then
        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StudentResponse.class)
                .value(response -> {
                    assert response.getFirstName().equals("Juan");
                    assert response.getDocumentNumber().equals("12345678");
                });
    }

    @Test
    @DisplayName("POST /api/students - Debe retornar 400 con datos inválidos")
    void shouldReturn400WithInvalidData() {
        // Given
        CreateStudentRequest invalidRequest = new CreateStudentRequest();
        // No se establecen campos requeridos

        // When & Then
        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Debe eliminar estudiante")
    void shouldDeleteStudent() {
        // Given
        String studentId = "test-id";
        when(studentService.delete(studentId)).thenReturn(Mono.empty());

        // When & Then
        webTestClient.delete()
                .uri("/api/students/{id}", studentId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("GET /api/students/document/{documentNumber} - Debe encontrar por número de documento")
    void shouldFindByDocumentNumber() {
        // Given
        String documentNumber = "12345678";
        when(studentService.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(sampleResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/students/document/{documentNumber}", documentNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StudentResponse.class)
                .value(response -> {
                    assert response.getDocumentNumber().equals(documentNumber);
                });
    }

    @Test
    @DisplayName("GET /api/students/status/{status} - Debe encontrar por estado")
    void shouldFindByStatus() {
        // Given
        Status status = Status.ACTIVE;
        when(studentService.findByStatus(status)).thenReturn(Flux.just(sampleResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/students/status/{status}", status)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponse.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("GET /api/students/gender/{gender} - Debe encontrar por género")
    void shouldFindByGender() {
        // Given
        Gender gender = Gender.MALE;
        when(studentService.findByGender(gender)).thenReturn(Flux.just(sampleResponse));

        // When & Then
        webTestClient.get()
                .uri("/api/students/gender/{gender}", gender)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponse.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("PUT /api/students/{id}/restore - Debe restaurar estudiante")
    void shouldRestoreStudent() {
        // Given
        String studentId = "test-id";
        when(studentService.restore(studentId)).thenReturn(Mono.just(sampleResponse));

        // When & Then
        webTestClient.put()
                .uri("/api/students/{id}/restore", studentId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StudentResponse.class)
                .value(response -> {
                    assert response.getId().equals(studentId);
                });
    }

    @Test
    @DisplayName("GET /api/students/stats/enrollment - Debe retornar estadísticas de matrícula")
    void shouldReturnEnrollmentStats() {
        // Given
        when(studentService.getEnrollmentStats()).thenReturn(Mono.just(new java.util.HashMap<>()));

        // When & Then
        webTestClient.get()
                .uri("/api/students/stats/enrollment")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    // Métodos auxiliares

    private StudentResponse createSampleResponse() {
        return StudentResponse.builder()
                .id("test-id")
                .firstName("Juan")
                .lastName("Pérez")
                .documentType(DocumentType.DNI)
                .documentNumber("12345678")
                .birthDate(LocalDate.of(2010, 1, 1))
                .gender(Gender.MALE)
                .address("Av. Principal 123")
                .district("Lima")
                .province("Lima")
                .department("Lima")
                .phone("987654321")
                .email("juan@email.com")
                .guardianName("Pedro")
                .guardianLastName("Pérez")
                .guardianDocumentType(DocumentType.DNI)
                .guardianDocumentNumber("87654321")
                .guardianPhone("123456789")
                .guardianEmail("pedro@email.com")
                .guardianRelationship(GuardianRelationship.FATHER)
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
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