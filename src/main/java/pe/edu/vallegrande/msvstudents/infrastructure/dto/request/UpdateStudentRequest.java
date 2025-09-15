package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UpdateStudentRequest {
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

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid phone format")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    // Guardian Information
    private String guardianName;
    private String guardianLastName;
    private DocumentType guardianDocumentType;
    private String guardianDocumentNumber;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid guardian phone format")
    private String guardianPhone;

    @Email(message = "Invalid guardian email format")
    private String guardianEmail;

    private GuardianRelationship guardianRelationship;
    private Status status;
}
