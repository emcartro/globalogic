package com.globallogic.app.users_app.model.dto.request;

import com.globallogic.app.users_app.consts.ConstantesApp;
import com.globallogic.app.users_app.model.entity.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(message =  "{app.message.valid.size.min.max}", min = 1, max = 60)
    @Schema(description = "Username del usuario.",example = "testgcr@test.com")
    @Pattern(regexp = ConstantesApp.PATTERN_CORREO, message = "El campo email debe ser un correo electrónico válido.")
    private String email;

    @NotBlank
    @Pattern(regexp = ConstantesApp.PATTERN_PASSWORD, message = "El password no cumple los criterios de seguridad.")
    private String password;

    private String name;
    private List<Phone> phones;
}