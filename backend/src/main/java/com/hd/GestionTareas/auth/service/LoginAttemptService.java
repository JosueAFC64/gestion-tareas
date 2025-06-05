package com.hd.GestionTareas.auth.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final ConcurrentHashMap<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        LoginAttempt attempt = attemptsCache.getOrDefault(key, new LoginAttempt());
        attempt.incrementAttempts();
        attempt.setLastAttempt(System.currentTimeMillis());
        attemptsCache.put(key, attempt);
    }

    public boolean isBlocked(String key) {
        LoginAttempt attempt = attemptsCache.get(key);
        if(attempt == null) {
            return false;
        }

        // Si ha pasado el tiempo de bloqueo, limpiar el registro
        long BLOCK_TIME_MS = 5 * 60 * 1000;
        if(System.currentTimeMillis() - attempt.getLastAttempt() > BLOCK_TIME_MS) {
            attemptsCache.remove(key);
            return false;
        }

        int MAX_ATTEMPTS = 3;
        return attempt.getAttempts() >= MAX_ATTEMPTS;
    }

    @Getter
    @Setter
    private static class LoginAttempt {
        private int attempts;
        private long lastAttempt;

        public void incrementAttempts() {
            this.attempts++;
        }
    }

}
