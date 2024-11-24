package org.example.task_manager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.example.task_manager.dto.SingInRequest;
import org.example.task_manager.exceptionHandler.TokenExpiredException;
import org.example.task_manager.exceptionHandler.UnauthorizedException;
import org.example.task_manager.models.Session;
import org.example.task_manager.models.User;
import org.example.task_manager.repository.SessionRepository;
import org.example.task_manager.repository.UserRepository;
import org.example.task_manager.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;


    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public String generateRefreshToken(User user) {
        String refreshToken = JWTUtil.generateRefreshToken(user);
        return refreshToken;
    }

    public String generateAccessToken(User user) {
        String accessToken = JWTUtil.generateAccessToken(user);
        return accessToken;
    }

    public void saveSession(String refreshToken, String accessToken, User user) {
        Date expiredDateRefresh = JWTUtil.extractClaim(refreshToken).getExpiration();


        Session session = new Session();
        session.setUser(user);
        session.setRefreshToken(refreshToken);
        session.setAccessToken(accessToken);
        session.setExpiredDateRefresh(expiredDateRefresh);
        sessionRepository.save(session);
    }

    public String getDataFromAccessToken(String token) throws UnauthorizedException {

        try {
            Claims claims = JWTUtil.extractClaim(token);
            return claims.get("role", String.class);
        } catch (JwtException e) {
            throw new UnauthorizedException("Невалидный токен или ошибка при его обработке");
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (JWTUtil.validateToken(refreshToken)) {

            Session session = sessionRepository.getSessionsByRefreshToken(refreshToken);

            User user = session.getUser();

            String accessToken = JWTUtil.generateAccessToken(user);


            session.setAccessToken(accessToken);

            sessionRepository.save(session);

            return accessToken;
        } else {
            throw new TokenExpiredException("Авторизуйтесь снова, refresh токен истек");
        }

    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Session getSessionByUserId(User user) {
        return sessionRepository.getSessionsByUser(user);
    }

    public Map<String, String> getJWT(SingInRequest singInRequest) {
        User user = findUserByEmail(singInRequest.getEmail());
        Map<String, String> jwtTokens = new HashMap<>();
        Session session = getSessionByUserId(user);


        if (session != null && session.getExpiredDateRefresh().after(new Date())) {
            jwtTokens.put("accessToken", session.getAccessToken());
            jwtTokens.put("refreshToken", session.getRefreshToken());
        } else {

            String accessToken = generateAccessToken(user);
            String refreshToken = generateRefreshToken(user);


            saveSession(refreshToken, accessToken, user);

            jwtTokens.put("accessToken", accessToken);
            jwtTokens.put("refreshToken", refreshToken);
        }
        return jwtTokens;
    }

}
