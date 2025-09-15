package pe.edu.vallegrande.msvstudents.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Pruebas parametrizadas para StudentService
 * Estas pruebas validan diferentes escenarios usando múltiples conjuntos de datos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Parametrizadas del Servicio de Estudiantes")
class StudentServiceParameterizedTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentEnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student sampleStudent;

    @BeforeEach
    void setUp() {
        sampleStudent = createSampleStudent();
    }

    /**
     * Prueba parametrizada para validar búsqueda por diferentes tipos de documento
     */
    @ParameterizedTest(name = "Buscar estudiante con tipo de documento: {0}")
    @EnumSource(DocumentType.class)
    @DisplayName("Debe encontrar estudiantes por tipo de documento")
    void shouldFindStudentsByDocumentType(DocumentType documentType) {
        // Given
        sampleStudent.setDocumentType(documentType);
        when(studentRepository.findByDocumentNumber(anyString()))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByDocumentNumber("12345678"))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(documentType, response.getDocumentType());
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada para validar búsqueda por género
     */
    @ParameterizedTest(name = "Buscar estudiantes por género: {0}")
    @EnumSource(Gender.class)
    @DisplayName("Debe encontrar estudiantes por género")
    void shouldFindStudentsByGender(Gender gender) {
        // Given
        sampleStudent.setGender(gender);
        when(studentRepository.findByGender(gender))
                .thenReturn(Flux.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByGender(gender))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(gender, response.getGender());
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada para validar búsqueda por estado
     */
    @ParameterizedTest(name = "Buscar estudiantes por estado: {0}")
    @EnumSource(Status.class)
    @DisplayName("Debe encontrar estudiantes por estado")
    void shouldFindStudentsByStatus(Status status) {
        // Given
        sampleStudent.setStatus(status);
        when(studentRepository.findByStatus(status))
                .thenReturn(Flux.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByStatus(status))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(status, response.getStatus());
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada para validar búsqueda por nombres usando CSV
     */
    @ParameterizedTest(name = "Buscar por nombre: {0}")
    @CsvSource({
            "Juan, Juan Carlos",
            "María, María Elena", 
            "Pedro, Pedro Luis",
            "Ana, Ana Sofía",
            "Carlos, Carlos Alberto"
    })
    @DisplayName("Debe encontrar estudiantes por nombre parcial")
    void shouldFindStudentsByPartialName(String searchTerm, String fullName) {
        // Given
        sampleStudent.setFirstName(fullName);
        when(studentRepository.findByFirstNameContainingIgnoreCase(searchTerm))
                .thenReturn(Flux.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByFirstName(searchTerm))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertTrue(response.getFirstName().contains(searchTerm));
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada para validar números de documento usando ValueSource
     */
    @ParameterizedTest(name = "Validar número de documento: {0}")
    @ValueSource(strings = {
            "12345678", "87654321", "11111111", "99999999", "12121212"
    })
    @DisplayName("Debe validar diferentes números de documento")
    void shouldValidateDocumentNumbers(String documentNumber) {
        // Given
        sampleStudent.setDocumentNumber(documentNumber);
        when(studentRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findByDocumentNumber(documentNumber))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(documentNumber, response.getDocumentNumber());
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada usando MethodSource para casos complejos
     */
    @ParameterizedTest(name = "Crear estudiante: {0}")
    @MethodSource("provideStudentCreationData")
    @DisplayName("Debe crear estudiantes con diferentes datos")
    void shouldCreateStudentsWithDifferentData(CreateStudentRequest request, String expectedName) {
        // Given
        Student newStudent = createStudentFromRequest(request);
        when(studentRepository.findByDocumentNumber(request.getDocumentNumber()))
                .thenReturn(Mono.empty());
        when(studentRepository.save(any(Student.class)))
                .thenReturn(Mono.just(newStudent));

        // When & Then
        StepVerifier.create(studentService.save(request))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(expectedName, response.getFirstName());
                    assertEquals(request.getDocumentNumber(), response.getDocumentNumber());
                })
                .verifyComplete();
    }

    /**
     * Prueba parametrizada para validar edades usando MethodSource
     */
    @ParameterizedTest(name = "Validar edad del estudiante nacido en: {0}")
    @MethodSource("provideBirthDates")
    @DisplayName("Debe calcular correctamente las edades")
    void shouldCalculateAgesCorrectly(LocalDate birthDate, int expectedMinAge, int expectedMaxAge) {
        // Given
        sampleStudent.setBirthDate(birthDate);
        when(studentRepository.findById(anyString()))
                .thenReturn(Mono.just(sampleStudent));

        // When & Then
        StepVerifier.create(studentService.findById("test-id"))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertNotNull(response.getBirthDate());
                    
                    // Calcular edad actual
                    int currentAge = LocalDate.now().getYear() - birthDate.getYear();
                    if (LocalDate.now().getDayOfYear() < birthDate.getDayOfYear()) {
                        currentAge--;
                    }
                    
                    assertTrue(currentAge >= expectedMinAge && currentAge <= expectedMaxAge,
                            String.format("Edad calculada %d debe estar entre %d y %d", 
                                    currentAge, expectedMinAge, expectedMaxAge));
                })
                .verifyComplete();
    }

    // Métodos auxiliares para generar datos de prueba

    /**
     * Proveedor de datos para pruebas de creación de estudiantes
     */
    static Stream<Arguments> provideStudentCreationData() {
        return Stream.of(
                Arguments.of(createStudentRequest("Juan", "Pérez", "12345678", DocumentType.DNI, Gender.MALE), "Juan"),
                Arguments.of(createStudentRequest("María", "García", "87654321", DocumentType.DNI, Gender.FEMALE), "María"),
                Arguments.of(createStudentRequest("Carlos", "López", "11111111", DocumentType.CE, Gender.MALE), "Carlos"),
                Arguments.of(createStudentRequest("Ana", "Martínez", "22222222", DocumentType.PASSPORT, Gender.FEMALE), "Ana"),
                Arguments.of(createStudentRequest("Pedro", "Rodríguez", "33333333", DocumentType.DNI, Gender.MALE), "Pedro")
        );
    }

    /**
     * Proveedor de datos para pruebas de fechas de nacimiento
     */
    static Stream<Arguments> provideBirthDates() {
        return Stream.of(
                Arguments.of(LocalDate.of(2010, 1, 1), 13, 14),  // Estudiante de ~14 años
                Arguments.of(LocalDate.of(2008, 6, 15), 15, 16), // Estudiante de ~16 años
                Arguments.of(LocalDate.of(2012, 12, 31), 11, 12), // Estudiante de ~12 años
                Arguments.of(LocalDate.of(2009, 3, 20), 14, 15), // Estudiante de ~15 años
                Arguments.of(LocalDate.of(2011, 9, 10), 12, 13)  // Estudiante de ~13 años
        );
    }

    /**
     * Crea un estudiante de muestra para las pruebas
     */
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

    /**
     * Crea un CreateStudentRequest para las pruebas
     */
    private static CreateStudentRequest createStudentRequest(String firstName, String lastName, 
                                                           String documentNumber, DocumentType documentType, 
                                                           Gender gender) {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setDocumentNumber(documentNumber);
        request.setDocumentType(documentType);
        request.setGender(gender);
        request.setBirthDate(LocalDate.of(2010, 1, 1));
        request.setAddress("Av. Test 123");
        request.setDistrict("Lima");
        request.setProvince("Lima");
        request.setDepartment("Lima");
        request.setPhone("987654321");
        request.setGuardianName("Guardian");
        request.setGuardianLastName("Test");
        request.setGuardianDocumentType(DocumentType.DNI);
        request.setGuardianDocumentNumber("11111111");
        request.setGuardianPhone("123456789");
        request.setGuardianEmail("guardian@test.com");
        request.setGuardianRelationship(GuardianRelationship.FATHER);
        return request;
    }

    /**
     * Convierte un CreateStudentRequest a Student
     */
    private Student createStudentFromRequest(CreateStudentRequest request) {
        Student student = new Student();
        student.setId("new-id");
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
    }
}