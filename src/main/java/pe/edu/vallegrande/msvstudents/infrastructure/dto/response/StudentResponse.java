package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import lombok.Data;
import lombok.Builder;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {
    private String id;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String district;
    private String province;
    private String department;
    private String phone;
    private String email;
    
    // Guardian Information
    private String guardianName;
    private String guardianLastName;
    private DocumentType guardianDocumentType;
    private String guardianDocumentNumber;
    private String guardianPhone;
    private String guardianEmail;
    private GuardianRelationship guardianRelationship;
    
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 