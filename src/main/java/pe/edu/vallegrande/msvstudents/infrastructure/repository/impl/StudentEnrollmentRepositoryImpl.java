package pe.edu.vallegrande.msvstudents.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentEnrollmentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class StudentEnrollmentRepositoryImpl implements StudentEnrollmentRepository {
    
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<StudentEnrollment> findAll() {
        return mongoTemplate.findAll(StudentEnrollment.class);
    }

    @Override
    public Mono<StudentEnrollment> findById(String id) {
        return mongoTemplate.findById(id, StudentEnrollment.class);
    }

    @Override
    public Mono<StudentEnrollment> save(StudentEnrollment enrollment) {
        return mongoTemplate.save(enrollment);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(
            Query.query(Criteria.where("id").is(id)),
            StudentEnrollment.class
        ).then();
    }

    @Override
    public Flux<StudentEnrollment> findByStudentId(String studentId) {
        return mongoTemplate.find(
            Query.query(Criteria.where("studentId").is(studentId)),
            StudentEnrollment.class
        );
    }

    @Override
    public Flux<StudentEnrollment> findByClassroomId(String classroomId) {
        return mongoTemplate.find(
            Query.query(Criteria.where("classroomId").is(classroomId)),
            StudentEnrollment.class
        );
    }

    @Override
    public Mono<StudentEnrollment> findByEnrollmentNumber(String enrollmentNumber) {
        return mongoTemplate.findOne(
            Query.query(Criteria.where("enrollmentNumber").is(enrollmentNumber)),
            StudentEnrollment.class
        );
    }

    @Override
    public Flux<StudentEnrollment> findByStatus(String status) {
        return mongoTemplate.find(
            Query.query(Criteria.where("status").is(status)),
            StudentEnrollment.class
        );
    }

    @Override
    public Flux<StudentEnrollment> findAllByOrderByCreatedAtAsc() {
        Query query = new Query().with(Sort.by(Sort.Order.asc("createdAt")));
        return mongoTemplate.find(query, StudentEnrollment.class);
    }

    @Override
    public Flux<StudentEnrollment> findByStudentIdOrderByCreatedAtAsc(String studentId) {
        Query query = Query.query(Criteria.where("studentId").is(studentId))
                          .with(Sort.by(Sort.Order.asc("createdAt")));
        return mongoTemplate.find(query, StudentEnrollment.class);
    }

    @Override
    public Flux<StudentEnrollment> findByStudentIdAndStatus(String studentId, String status) {
        Query query = Query.query(
            Criteria.where("studentId").is(studentId)
                   .and("status").is(status)
        );
        return mongoTemplate.find(query, StudentEnrollment.class);
    }

    @Override
    public Flux<StudentEnrollment> findByStudentIdAndClassroomIdAndStatus(String studentId, String classroomId, String status) {
        Query query = Query.query(
            Criteria.where("studentId").is(studentId)
                   .and("classroomId").is(classroomId)
                   .and("status").is(status)
        );
        return mongoTemplate.find(query, StudentEnrollment.class);
    }

    @Override
    public Mono<Long> countByStatus(String status) {
        Query query = Query.query(Criteria.where("status").is(status));
        return mongoTemplate.count(query, StudentEnrollment.class);
    }
}
