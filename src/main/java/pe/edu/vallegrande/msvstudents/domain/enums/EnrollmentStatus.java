package pe.edu.vallegrande.msvstudents.domain.enums;

public enum EnrollmentStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    COMPLETED("COMPLETED"),
    TRANSFERRED("TRANSFERRED"),
    WITHDRAWN("WITHDRAWN"),
    SUSPENDED("SUSPENDED");

    private final String value;

    EnrollmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
