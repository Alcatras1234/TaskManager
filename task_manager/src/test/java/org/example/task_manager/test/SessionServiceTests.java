package org.example.task_manager.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.example.task_manager.dto.SingInRequest;
import org.example.task_manager.enums.RoleEnum;
import org.example.task_manager.exceptionHandler.TokenExpiredException;
import org.example.task_manager.exceptionHandler.UnauthorizedException;
import org.example.task_manager.models.Session;
import org.example.task_manager.models.User;
import org.example.task_manager.repository.SessionRepository;
import org.example.task_manager.repository.UserRepository;
import org.example.task_manager.service.SessionService;
import org.example.task_manager.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionServiceTests {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private User user;
    @InjectMocks
    private SessionService sessionService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void testGenerateRefreshTokenStructure() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");

        String refreshToken = sessionService.generateRefreshToken(user);

        Claims claims = JWTUtil.extractClaim(refreshToken);
        assertEquals("1", claims.getSubject());
        assertEquals("test@example.com", claims.get("email", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void testGenerateAccessTokenStructure() {
        User user = new User();
        user.setUserId(1);
        user.setRole(RoleEnum.USER);


        String accessToken = sessionService.generateAccessToken(user);

        Claims claims = JWTUtil.extractClaim(accessToken);
        assertEquals("1", claims.getSubject());
        assertEquals("USER", claims.get("role", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void testSaveSession() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);


        String refreshToken = JWTUtil.generateRefreshToken(user);
        String accessToken = JWTUtil.generateAccessToken(user);


        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {

            Claims claims = mock(Claims.class);
            mockedJWTUtil.when(() -> JWTUtil.extractClaim(refreshToken)).thenReturn(claims);
            when(claims.getExpiration()).thenReturn(new Date());


            sessionService.saveSession(refreshToken, accessToken, user);


            verify(sessionRepository, times(1)).save(any(Session.class));


            mockedJWTUtil.verify(() -> JWTUtil.extractClaim(refreshToken));
        }
    }

    @Test
    void testGetDataFromAccessToken_ValidToken() throws UnauthorizedException {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);
        String token = JWTUtil.generateAccessToken(user);

        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {
            Claims claims = mock(Claims.class);
            mockedJWTUtil.when(() -> JWTUtil.extractClaim(token)).thenReturn(claims);
            when(claims.get("role", String.class)).thenReturn("USER");

            String role = sessionService.getDataFromAccessToken(token);

            assertEquals("USER", role);
        }
    }

    @Test
    void testGetDataFromAccessToken_InvalidToken() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);
        String token = JWTUtil.generateAccessToken(user);

        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {
            mockedJWTUtil.when(() -> JWTUtil.extractClaim(token)).thenThrow(new JwtException("Invalid token"));

            assertThrows(UnauthorizedException.class, () -> sessionService.getDataFromAccessToken(token));
        }
    }

    @Test
    void testRefreshAccessToken_ValidRefreshToken() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);
        Session session = new Session();
        session.setUser(user);
        String refreshToken = JWTUtil.generateRefreshToken(user);

        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {
            mockedJWTUtil.when(() -> JWTUtil.validateToken(refreshToken)).thenReturn(true);
            when(sessionRepository.getSessionsByRefreshToken(refreshToken)).thenReturn(session);
            mockedJWTUtil.when(() -> JWTUtil.generateAccessToken(user)).thenReturn("newAccessToken");

            String newAccessToken = sessionService.refreshAccessToken(refreshToken);

            assertEquals("newAccessToken", newAccessToken);
            verify(sessionRepository, times(1)).save(any(Session.class));

            mockedJWTUtil.verify(() -> JWTUtil.validateToken(refreshToken));

            mockedJWTUtil.verify(() -> JWTUtil.generateAccessToken(user));
        }
    }

    @Test
    void testRefreshAccessToken_ExpiredToken() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);
        String refreshToken = JWTUtil.generateRefreshToken(user);
        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {
            mockedJWTUtil.when(() -> JWTUtil.validateToken(refreshToken)).thenReturn(false);

            assertThrows(TokenExpiredException.class, () -> sessionService.refreshAccessToken(refreshToken));
        }
    }

    @Test
    void testGetJWT_ExistingSession() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);
        user.setEmail("test@example.com");
        Session session = new Session();
        session.setAccessToken("accessToken");
        session.setRefreshToken("refreshToken");
        session.setExpiredDateRefresh(new Date(System.currentTimeMillis() + 10000));

        SingInRequest singInRequest = new SingInRequest();
        singInRequest.setEmail("test@example.com");

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
        when(sessionRepository.getSessionsByUser(user)).thenReturn(session);

        Map<String, String> tokens = sessionService.getJWT(singInRequest);

        assertEquals("accessToken", tokens.get("accessToken"));
        assertEquals("refreshToken", tokens.get("refreshToken"));
    }

    @Test
    void testGetJWT_NewSession() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setRole(RoleEnum.USER);

        SingInRequest singInRequest = new SingInRequest();
        singInRequest.setEmail("test@example.com");

        try (MockedStatic<JWTUtil> mockedJWTUtil = mockStatic(JWTUtil.class)) {
            when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
            when(sessionRepository.getSessionsByUser(user)).thenReturn(null);
            mockedJWTUtil.when(() -> JWTUtil.generateAccessToken(user)).thenReturn("validAccessToken");
            mockedJWTUtil.when(() -> JWTUtil.generateRefreshToken(user)).thenReturn("validRefreshToken");

            Claims mockClaims = mock(Claims.class);
            when(mockClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600 * 1000));
            mockedJWTUtil.when(() -> JWTUtil.extractClaim("validRefreshToken")).thenReturn(mockClaims);

            Map<String, String> tokens = sessionService.getJWT(singInRequest);


            assertEquals("validAccessToken", tokens.get("accessToken"));
            assertEquals("validRefreshToken", tokens.get("refreshToken"));
            verify(sessionRepository, times(1)).save(argThat(session -> {
                assertNotNull(session);
                assertEquals("validRefreshToken", session.getRefreshToken());
                assertEquals(user, session.getUser());
                return true;
            }));
        }
    }
}
