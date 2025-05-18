package com.DressKlub.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private String accessToken;

    public AuthenticationResponse(String accessToken) {
    }

}