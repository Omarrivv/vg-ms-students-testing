package pe.edu.vallegrande.msvstudents.domain.enums;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    TRANSFERRED("TRANSFERRED"),
    GRADUATED("GRADUATED"),
    DECEASED("DECEASED");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 