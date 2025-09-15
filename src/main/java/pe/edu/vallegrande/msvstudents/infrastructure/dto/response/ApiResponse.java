package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Metadata metadata;
    private T data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private Integer status;
        private String message;
        private LocalDateTime timestamp;
    }

    public static <T> ApiResponse<T> success(T data, String message, int status) {
        Metadata metadata = new Metadata(status, message, LocalDateTime.now());
        return new ApiResponse<>(metadata, data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, 200);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        Metadata metadata = new Metadata(status, message, LocalDateTime.now());
        return new ApiResponse<>(metadata, null);
    }
}
