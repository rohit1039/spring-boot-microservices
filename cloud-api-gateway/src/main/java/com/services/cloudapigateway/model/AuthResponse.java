package com.services.cloudapigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse
{
    private String userId;
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;
    private List<String> authorityList;
}
