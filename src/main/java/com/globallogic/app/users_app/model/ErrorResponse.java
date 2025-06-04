package com.globallogic.app.users_app.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private List<ErrorDetail> error; // Matches "error": [...]

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder // For consistency
    public static class ErrorDetail {
        private LocalDateTime timestamp;
        private Integer codigo;
        private String detail;
    }
}