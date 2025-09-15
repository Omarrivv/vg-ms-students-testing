package pe.edu.vallegrande.msvstudents.infrastructure.dto.request;

import lombok.Data;
import pe.edu.vallegrande.msvstudents.domain.enums.DocumentType;
import pe.edu.vallegrande.msvstudents.domain.enums.Gender;
import pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class CreateStudentRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Address is required")
    private String address;

    private String district;
    private String province;
    private String department;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid phone format")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    // Guardian Information
    @NotBlank(message = "Guardian name is required")
    private String guardianName;

    @NotBlank(message = "Guardian last name is required")
    private String guardianLastName;

    @NotNull(message = "Guardian document type is required")
    private DocumentType guardianDocumentType;

    @NotBlank(message = "Guardian document number is required")
    private String guardianDocumentNumber;

    @NotBlank(message = "Guardian phone is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Invalid guardian phone format")
    private String guardianPhone;

    @Email(message = "Invalid guardian email format")
    private String guardianEmail;

    @NotNull(message = "Guardian relationship is required")
    private GuardianRelationship guardianRelationship;
}
