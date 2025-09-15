package pe.edu.vallegrande.msvstudents.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.msvstudents.application.service.StudentEnrollmentService;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentEnrollmentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentEnrollmentResponse;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final StudentEnrollmentService enrollmentService;

    @GetMapping
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> findAll() {
        return enrollmentService.findAllOrderedByCreatedAt()
                .collectList()
                .map(enrollments -> ApiResponse.success(enrollments, "Student enrollments retrieved successfully"));
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse<StudentEnrollmentResponse>> findById(@PathVariable String id) {
        return enrollmentService.findById(id)
                .map(enrollment -> ApiResponse.success(enrollment, "Student enrollment retrieved successfully"));
    }

    @PostMapping
    public Mono<ApiResponse<StudentEnrollmentResponse>> save(@Valid @RequestBody CreateStudentEnrollmentRequest request) {
        return enrollmentService.save(request)
                .map(enrollment -> ApiResponse.success(enrollment, "Student enrollment created successfully", 201));
    }

    @PutMapping("/{id}/status/{status}")
    public Mono<ApiResponse<StudentEnrollmentResponse>> updateStatus(@PathVariable String id, 
                                                                     @PathVariable String status) {
        return enrollmentService.updateStatus(id, status)
                .map(enrollment -> ApiResponse.success(enrollment, "Student enrollment status updated successfully"));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<Void>> delete(@PathVariable String id) {
        return enrollmentService.delete(id)
                .then(Mono.just(ApiResponse.success(null, "Student enrollment deactivated successfully", 204)));
    }

    @PutMapping("/{id}/restore")
    public Mono<ApiResponse<StudentEnrollmentResponse>> restore(@PathVariable String id) {
        return enrollmentService.restore(id)
                .map(enrollment -> ApiResponse.success(enrollment, "Student enrollment restored successfully"));
    }

    @GetMapping("/student/{studentId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> findByStudentId(@PathVariable String studentId) {
        return enrollmentService.findByStudentIdOrderedByCreatedAt(studentId)
                .collectList()
                .map(enrollments -> ApiResponse.success(enrollments, "Student enrollments retrieved by student ID successfully"));
    }

    @GetMapping("/classroom/{classroomId}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> findByClassroomId(@PathVariable String classroomId) {
        return enrollmentService.findByClassroomId(classroomId)
                .collectList()
                .map(enrollments -> ApiResponse.success(enrollments, "Student enrollments retrieved by classroom ID successfully"));
    }

    @GetMapping("/enrollment-number/{enrollmentNumber}")
    public Mono<ApiResponse<StudentEnrollmentResponse>> findByEnrollmentNumber(@PathVariable String enrollmentNumber) {
        return enrollmentService.findByEnrollmentNumber(enrollmentNumber)
                .map(enrollment -> ApiResponse.success(enrollment, "Student enrollment retrieved by enrollment number successfully"));
    }

    @GetMapping("/status/{status}")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> findByStatus(@PathVariable String status) {
        return enrollmentService.findByStatus(status)
                .collectList()
                .map(enrollments -> ApiResponse.success(enrollments, "Student enrollments retrieved by status successfully"));
    }

    @PostMapping("/bulk")
    public Mono<ApiResponse<List<StudentEnrollmentResponse>>> saveBulk(@Valid @RequestBody List<CreateStudentEnrollmentRequest> requests) {
        return enrollmentService.saveBulk(requests)
                .collectList()
                .map(enrollments -> ApiResponse.success(enrollments, "Student enrollments created successfully in bulk", 201));
    }

    @GetMapping("/analytics/classroom/{classroomId}/stats")
    public Mono<ApiResponse<Object>> getClassroomStats(@PathVariable String classroomId) {
        return enrollmentService.getClassroomStats(classroomId)
                .map(stats -> ApiResponse.success(stats, "Classroom statistics retrieved successfully"));
    }

    @GetMapping("/analytics/enrollment-distribution")
    public Mono<ApiResponse<Object>> getEnrollmentDistribution() {
        return enrollmentService.getEnrollmentDistribution()
                .map(distribution -> ApiResponse.success(distribution, "Enrollment distribution retrieved successfully"));
    }
}
