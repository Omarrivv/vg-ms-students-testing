package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.msvstudents.application.service.StudentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Mono<ApiResponse<List<StudentResponse>>> findAll() {
        return studentService.findAllOrderedByCreatedAt()
                .collectList()
                .map(students -> ApiResponse.success(students, "Students retrieved successfully"));
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<StudentResponse>> findById(@PathVariable String id) {
        return studentService.findById(id)
                .map(student -> ApiResponse.success(student, "Student retrieved successfully"));
    }

    @PostMapping
    public Mono<ApiResponse<StudentResponse>> save(@Valid @RequestBody CreateStudentRequest request) {
        return studentService.save(request)
                .map(student -> ApiResponse.success(student, "Student created successfully", 201));
    }

    @PutMapping("/{id}")
    public Mono<ApiResponse<StudentResponse>> update(@PathVariable String id, 
                                                     @Valid @RequestBody UpdateStudentRequest request) {
        return studentService.update(id, request)
                .map(student -> ApiResponse.success(student, "Student updated successfully"));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Void>> delete(@PathVariable String id) {
        return studentService.delete(id)
                .then(Mono.just(ApiResponse.success(null, "Student deleted successfully", 204)));
    }

    @GetMapping("/document/{documentNumber}")
    public Mono<ApiResponse<StudentResponse>> findByDocumentNumber(@PathVariable String documentNumber) {
        return studentService.findByDocumentNumber(documentNumber)
                .map(student -> ApiResponse.success(student, "Student retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    public Mono<ApiResponse<List<StudentResponse>>> findByStatus(@PathVariable Status status) {
        return studentService.findByStatus(status)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students retrieved by status successfully"));
    }

    @GetMapping("/gender/{gender}")
    public Mono<ApiResponse<List<StudentResponse>>> findByGender(@PathVariable Gender gender) {
        return studentService.findByGender(gender)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students retrieved by gender successfully"));
    }

    @GetMapping("/search/firstname/{firstName}")
    public Mono<ApiResponse<List<StudentResponse>>> findByFirstName(@PathVariable String firstName) {
        return studentService.findByFirstName(firstName)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students retrieved by first name successfully"));
    }

    @GetMapping("/search/lastname/{lastName}")
    public Mono<ApiResponse<List<StudentResponse>>> findByLastName(@PathVariable String lastName) {
        return studentService.findByLastName(lastName)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students retrieved by last name successfully"));
    }

    @PutMapping("/{id}/restore")
    public Mono<ApiResponse<StudentResponse>> restore(@PathVariable String id) {
        return studentService.restore(id)
                .map(student -> ApiResponse.success(student, "Student restored successfully"));
    }

    @PostMapping("/bulk")
    public Mono<ApiResponse<List<StudentResponse>>> saveBulk(@Valid @RequestBody List<CreateStudentRequest> requests) {
        return studentService.saveBulk(requests)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students created successfully in bulk", 201));
    }

    @GetMapping("/not-enrolled")
    public Mono<ApiResponse<List<StudentResponse>>> findStudentsNotEnrolled() {
        return studentService.findStudentsNotEnrolled()
                .collectList()
                .map(students -> ApiResponse.success(students, "Students not enrolled retrieved successfully"));
    }

    @GetMapping("/not-enrolled/classroom/{classroomId}")
    public Mono<ApiResponse<List<StudentResponse>>> findStudentsNotEnrolledInClassroom(@PathVariable String classroomId) {
        return studentService.findStudentsNotEnrolledInClassroom(classroomId)
                .collectList()
                .map(students -> ApiResponse.success(students, "Students not enrolled in classroom retrieved successfully"));
    }

    @GetMapping("/enrollment-stats")
    public Mono<ApiResponse<Object>> getEnrollmentStats() {
        return studentService.getEnrollmentStats()
                .map(stats -> ApiResponse.success(stats, "Enrollment statistics retrieved successfully"));
    }
} 