package pe.edu.vallegrande.msvstudents.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    
    @Indexed(unique = true)
    private String documentNumber;
    
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String district;
    private String province;
    private String department;
    private String phone;
    private String email; // opcional para menores
    
    // Guardian Information
    private String guardianName;
    private String guardianLastName;
    private DocumentType guardianDocumentType;
    private String guardianDocumentNumber;
    private String guardianPhone;
    private String guardianEmail;
    private String guardianRelationship; // FATHER, MOTHER, GUARDIAN, GRANDPARENT, OTHER
    
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 