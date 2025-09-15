package pe.edu.vallegrande.msvstudents.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.model.StudentEnrollment;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.core.query.Query;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "pe.edu.vallegrande.msvstudents.infrastructure.repository")
public class MongoConfig {

    @Bean
    public CommandLineRunner initData(ReactiveMongoTemplate mongoTemplate) {
        return args -> {
            // Solo insertar datos de ejemplo si las colecciones están vacías
            mongoTemplate.count(new Query(), Student.class)
                .flatMap(count -> {
                    if (count == 0) {
                        LocalDateTime now = LocalDateTime.now();
                        
                        // Crear estudiantes con nueva estructura
                        Student student1 = new Student();
                        student1.setId(UUID.randomUUID().toString());
                        student1.setFirstName("Juan Carlos");
                        student1.setLastName("González Pérez");
                        student1.setDocumentType(DocumentType.DNI);
                        student1.setDocumentNumber("78901234");
                        student1.setGender(Gender.MALE);
                        student1.setBirthDate(LocalDate.of(2015, 3, 15));
                        student1.setAddress("Jr. Los Pinos 123");
                        student1.setDistrict("Lima");
                        student1.setProvince("Lima");
                        student1.setDepartment("Lima");
                        student1.setPhone("912345678");
                        student1.setEmail("juangonzalez@mail.com");
                        student1.setGuardianName("Carlos");
                        student1.setGuardianLastName("González");
                        student1.setGuardianDocumentType(DocumentType.DNI);
                        student1.setGuardianDocumentNumber("12345678");
                        student1.setGuardianPhone("987654321");
                        student1.setGuardianEmail("carlos.gonzalez@mail.com");
                        student1.setGuardianRelationship(GuardianRelationship.FATHER.getValue());
                        student1.setStatus(Status.ACTIVE);
                        student1.setCreatedAt(now);
                        student1.setUpdatedAt(now);

                        Student student2 = new Student();
                        student2.setId(UUID.randomUUID().toString());
                        student2.setFirstName("María Lucía");
                        student2.setLastName("Martínez López");
                        student2.setDocumentType(DocumentType.DNI);
                        student2.setDocumentNumber("78901235");
                        student2.setGender(Gender.FEMALE);
                        student2.setBirthDate(LocalDate.of(2014, 5, 20));
                        student2.setAddress("Av. Los Jardines 456");
                        student2.setDistrict("Miraflores");
                        student2.setProvince("Lima");
                        student2.setDepartment("Lima");
                        student2.setPhone("912345679");
                        student2.setEmail("marialucia@mail.com");
                        student2.setGuardianName("Rosa");
                        student2.setGuardianLastName("López");
                        student2.setGuardianDocumentType(DocumentType.DNI);
                        student2.setGuardianDocumentNumber("23456789");
                        student2.setGuardianPhone("987654322");
                        student2.setGuardianEmail("rosa.lopez@mail.com");
                        student2.setGuardianRelationship(GuardianRelationship.MOTHER.getValue());
                        student2.setStatus(Status.ACTIVE);
                        student2.setCreatedAt(now);
                        student2.setUpdatedAt(now);

                        Student student3 = new Student();
                        student3.setId(UUID.randomUUID().toString());
                        student3.setFirstName("Pablo Andrés");
                        student3.setLastName("Torres Vega");
                        student3.setDocumentType(DocumentType.DNI);
                        student3.setDocumentNumber("78901236");
                        student3.setGender(Gender.MALE);
                        student3.setBirthDate(LocalDate.of(2015, 7, 10));
                        student3.setAddress("Calle Los Olivos 789");
                        student3.setDistrict("San Borja");
                        student3.setProvince("Lima");
                        student3.setDepartment("Lima");
                        student3.setPhone("912345680");
                        student3.setEmail("pablotorres@mail.com");
                        student3.setGuardianName("Elena");
                        student3.setGuardianLastName("Vega");
                        student3.setGuardianDocumentType(DocumentType.DNI);
                        student3.setGuardianDocumentNumber("34567890");
                        student3.setGuardianPhone("987654323");
                        student3.setGuardianEmail("elena.vega@mail.com");
                        student3.setGuardianRelationship(GuardianRelationship.GUARDIAN.getValue());
                        student3.setStatus(Status.ACTIVE);
                        student3.setCreatedAt(now);
                        student3.setUpdatedAt(now);

                        return mongoTemplate.insertAll(List.of(student1, student2, student3))
                            .collectList()
                            .flatMapMany(students -> {
                                // Crear matrículas con nueva estructura
                                StudentEnrollment enrollment1 = new StudentEnrollment();
                                enrollment1.setId(UUID.randomUUID().toString());
                                enrollment1.setStudentId(students.get(0).getId());
                                enrollment1.setClassroomId("classroom-2024-001");
                                enrollment1.setEnrollmentNumber("ENR-2024-001");
                                enrollment1.setEnrollmentDate(LocalDate.of(2024, 3, 1));
                                enrollment1.setStatus("ACTIVE");
                                enrollment1.setCreatedAt(now);
                                enrollment1.setUpdatedAt(now);

                                StudentEnrollment enrollment2 = new StudentEnrollment();
                                enrollment2.setId(UUID.randomUUID().toString());
                                enrollment2.setStudentId(students.get(1).getId());
                                enrollment2.setClassroomId("classroom-2024-001");
                                enrollment2.setEnrollmentNumber("ENR-2024-002");
                                enrollment2.setEnrollmentDate(LocalDate.of(2024, 3, 1));
                                enrollment2.setStatus("ACTIVE");
                                enrollment2.setCreatedAt(now);
                                enrollment2.setUpdatedAt(now);

                                StudentEnrollment enrollment3 = new StudentEnrollment();
                                enrollment3.setId(UUID.randomUUID().toString());
                                enrollment3.setStudentId(students.get(2).getId());
                                enrollment3.setClassroomId("classroom-2024-002");
                                enrollment3.setEnrollmentNumber("ENR-2024-003");
                                enrollment3.setEnrollmentDate(LocalDate.of(2024, 3, 15));
                                enrollment3.setStatus("ACTIVE");
                                enrollment3.setCreatedAt(now);
                                enrollment3.setUpdatedAt(now);

                                return mongoTemplate.insertAll(List.of(enrollment1, enrollment2, enrollment3));
                            })
                            .then();
                    } else {
                        return Mono.empty();
                    }
                })
                .block();
        };
    }
} 