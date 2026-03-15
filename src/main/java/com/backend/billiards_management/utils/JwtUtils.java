package com.backend.billiards_management.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class JwtUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> decodePayload(String jwt) {
        try {
            String[] chunks = jwt.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot decode JWT", e);
        }
    }
}