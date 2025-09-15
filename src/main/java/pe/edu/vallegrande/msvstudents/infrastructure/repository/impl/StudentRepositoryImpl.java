package pe.edu.vallegrande.msvstudents.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.infrastructure.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    @Override
    public Mono<Student> findById(String id) {
        return mongoTemplate.findById(id, Student.class);
    }

    @Override
    public Mono<Student> save(Student student) {
        return mongoTemplate.save(student);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(student -> {
                    student.setStatus(Status.INACTIVE);
                    return save(student);
                })
                .then();
    }

    @Override
    public Mono<Student> findByDocumentNumber(String documentNumber) {
        return mongoTemplate.findOne(
            Query.query(Criteria.where("documentNumber").is(documentNumber)),
            Student.class
        );
    }

    @Override
    public Flux<Student> findByStatus(Status status) {
        return mongoTemplate.find(
            Query.query(Criteria.where("status").is(status)),
            Student.class
        );
    }

    @Override
    public Flux<Student> findByGender(Gender gender) {
        return mongoTemplate.find(
            Query.query(Criteria.where("gender").is(gender)),
            Student.class
        );
    }

    @Override
    public Flux<Student> findByFirstNameContainingIgnoreCase(String firstName) {
        Pattern pattern = Pattern.compile(firstName, Pattern.CASE_INSENSITIVE);
        return mongoTemplate.find(
            Query.query(Criteria.where("firstName").regex(pattern)),
            Student.class
        );
    }

    @Override
    public Flux<Student> findByLastNameContainingIgnoreCase(String lastName) {
        Pattern pattern = Pattern.compile(lastName, Pattern.CASE_INSENSITIVE);
        return mongoTemplate.find(
            Query.query(Criteria.where("lastName").regex(pattern)),
            Student.class
        );
    }

    @Override
    public Flux<Student> findAllByOrderByCreatedAtAsc() {
        Query query = new Query().with(Sort.by(Sort.Order.asc("createdAt")));
        return mongoTemplate.find(query, Student.class);
    }

    @Override
    public Mono<Long> countByStatus(Status status) {
        Query query = Query.query(Criteria.where("status").is(status));
        return mongoTemplate.count(query, Student.class);
    }
} 