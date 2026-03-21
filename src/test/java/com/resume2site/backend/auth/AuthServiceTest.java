package com.resume2site.backend.auth;

import com.resume2site.backend.auth.dto.LoginRequest;
import com.resume2site.backend.auth.dto.SignupRequest;
import com.resume2site.backend.common.exception.ConflictException;
import com.resume2site.backend.common.exception.UnauthorizedException;
import com.resume2site.backend.config.JwtProperties;
import com.resume2site.backend.security.jwt.JwtTokenProvider;
import com.resume2site.backend.user.domain.User;
import com.resume2site.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties("test-suite", "test-secret-test-secret-test-secret-123456", 60);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties);
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager, jwtTokenProvider, jwtProperties);
    }

    @Test
    void signupCreatesUserWithNormalizedEmailAndHashedPassword() {
        SignupRequest request = new SignupRequest(" Alice@Example.com ", "password123", "Alice Johnson");
        when(userRepository.existsByEmailIgnoreCase("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        var response = authService.signup(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("alice@example.com");
        assertThat(savedUser.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(savedUser.getStatus()).isEqualTo("ACTIVE");
        assertThat(response.user().email()).isEqualTo("alice@example.com");
        assertThat(response.accessToken()).isNotBlank();
    }

    @Test
    void signupRejectsDuplicateEmail() {
        when(userRepository.existsByEmailIgnoreCase("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signup(new SignupRequest("alice@example.com", "password123", "Alice")))
                .isInstanceOf(ConflictException.class)
                .hasMessage("An account with this email already exists");
    }

    @Test
    void loginRejectsInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> authService.login(new LoginRequest("alice@example.com", "password123")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void loginReturnsTokenForValidCredentials() {
        User user = new User();
        user.setId(42L);
        user.setEmail("alice@example.com");
        user.setFullName("Alice Johnson");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("alice@example.com", "password123"));
        when(userRepository.findByEmailIgnoreCase("alice@example.com")).thenReturn(Optional.of(user));

        var response = authService.login(new LoginRequest("alice@example.com", "password123"));

        assertThat(response.user().id()).isEqualTo(42L);
        assertThat(response.user().email()).isEqualTo("alice@example.com");
        assertThat(response.accessToken()).isNotBlank();
    }
}
