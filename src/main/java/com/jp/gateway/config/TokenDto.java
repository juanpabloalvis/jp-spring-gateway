package com.jp.gateway.config;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TokenDto {
    private final String token;

//    public TokenDto(String token) {
//        this.token = token;
//    }
}
