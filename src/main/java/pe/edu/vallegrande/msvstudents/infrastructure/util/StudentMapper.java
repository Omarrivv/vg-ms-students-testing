package pe.edu.vallegrande.msvstudents.infrastructure.util;

import pe.edu.vallegrande.msvstudents.domain.model.Student;
import pe.edu.vallegrande.msvstudents.domain.enums.Status;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.CreateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.request.UpdateStudentRequest;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.StudentResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentMapper {

    public static Student toEntity(CreateStudentRequest request) {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
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

    public static Student updateEntity(Student existing, UpdateStudentRequest request) {
        if (request.getFirstName() != null) existing.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existing.setLastName(request.getLastName());
        if (request.getDocumentType() != null) existing.setDocumentType(request.getDocumentType());
        if (request.getDocumentNumber() != null) existing.setDocumentNumber(request.getDocumentNumber());
        if (request.getBirthDate() != null) existing.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) existing.setGender(request.getGender());
        if (request.getAddress() != null) existing.setAddress(request.getAddress());
        if (request.getDistrict() != null) existing.setDistrict(request.getDistrict());
        if (request.getProvince() != null) existing.setProvince(request.getProvince());
        if (request.getDepartment() != null) existing.setDepartment(request.getDepartment());
        if (request.getPhone() != null) existing.setPhone(request.getPhone());
        if (request.getEmail() != null) existing.setEmail(request.getEmail());
        if (request.getGuardianName() != null) existing.setGuardianName(request.getGuardianName());
        if (request.getGuardianLastName() != null) existing.setGuardianLastName(request.getGuardianLastName());
        if (request.getGuardianDocumentType() != null) existing.setGuardianDocumentType(request.getGuardianDocumentType());
        if (request.getGuardianDocumentNumber() != null) existing.setGuardianDocumentNumber(request.getGuardianDocumentNumber());
        if (request.getGuardianPhone() != null) existing.setGuardianPhone(request.getGuardianPhone());
        if (request.getGuardianEmail() != null) existing.setGuardianEmail(request.getGuardianEmail());
        if (request.getGuardianRelationship() != null) existing.setGuardianRelationship(request.getGuardianRelationship().getValue());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }

    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .documentType(student.getDocumentType())
                .documentNumber(student.getDocumentNumber())
                .birthDate(student.getBirthDate())
                .gender(student.getGender())
                .address(student.getAddress())
                .district(student.getDistrict())
                .province(student.getProvince())
                .department(student.getDepartment())
                .phone(student.getPhone())
                .email(student.getEmail())
                .guardianName(student.getGuardianName())
                .guardianLastName(student.getGuardianLastName())
                .guardianDocumentType(student.getGuardianDocumentType())
                .guardianDocumentNumber(student.getGuardianDocumentNumber())
                .guardianPhone(student.getGuardianPhone())
                .guardianEmail(student.getGuardianEmail())
                .guardianRelationship(student.getGuardianRelationship() != null ? 
                    pe.edu.vallegrande.msvstudents.domain.enums.GuardianRelationship.valueOf(student.getGuardianRelationship()) : null)
                .status(student.getStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
