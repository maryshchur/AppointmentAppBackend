package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTDto {
    private String accessToken;
    private String refreshToken;
}
