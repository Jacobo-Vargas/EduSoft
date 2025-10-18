package com.uniquindio.edu.edusoft.config.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenStoreService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenStoreService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Guardar token con TTL (se recomienda guardar por jti, no todo el token)
    public void storeToken(String jti, String username) {
        long ttlMillis = 600_000; // 1 hora
        redisTemplate.opsForValue().set(jti, username, ttlMillis, TimeUnit.MILLISECONDS);
    }

    // Validar si token existe en Redis
    public boolean isTokenValid(String jti) {
        return redisTemplate.opsForValue().get(jti) != null;
    }

    // Eliminar token (logout)
    public void removeToken(String jti) {
        redisTemplate.delete(jti);
    }
}
