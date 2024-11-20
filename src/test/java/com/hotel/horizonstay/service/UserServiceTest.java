package com.hotel.horizonstay.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hotel.horizonstay.dto.UserDTO;
import com.hotel.horizonstay.entity.SystemUser;
import com.hotel.horizonstay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldRegisterUserSuccessfully() {
        UserDTO registrationRequest = new UserDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");

        SystemUser savedUser = new SystemUser();
        savedUser.setId(1); // Ensure the ID is not null

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(SystemUser.class))).thenReturn(savedUser);

        UserDTO response = userService.register(registrationRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("User Saved Successfully", response.getMessage());
    }

    @Test
    void register_shouldReturnConflictWhenUserAlreadyExists() {
        UserDTO registrationRequest = new UserDTO();
        registrationRequest.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new SystemUser()));

        UserDTO response = userService.register(registrationRequest);

        assertEquals(409, response.getStatusCode());
        assertEquals("User already exists", response.getMessage());
    }

    @Test
    void login_shouldReturnErrorWhenAuthenticationFails() {
        UserDTO loginRequest = new UserDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        doThrow(new RuntimeException("Authentication failed")).when(authenticationManager).authenticate(any());

        UserDTO response = userService.login(loginRequest);

        assertEquals(500, response.getStatusCode());
        assertEquals("Authentication failed", response.getError());
    }

    @Test
    void refreshToken_shouldRefreshTokenSuccessfully() {
        UserDTO refreshTokenRequest = new UserDTO();
        refreshTokenRequest.setToken("validToken");

        SystemUser user = new SystemUser();
        user.setEmail("test@example.com");

        when(jwtUtils.extractUsername("validToken")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.isTokenValid("validToken", user)).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("newJwtToken");

        UserDTO response = userService.refreshToken(refreshTokenRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully Refreshed Token", response.getMessage());
        assertEquals("newJwtToken", response.getToken());
    }

    @Test
    void getAllUsers_shouldReturnAllUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(List.of(new SystemUser()));

        UserDTO response = userService.getAllUsers();

        assertEquals(200, response.getStatusCode());
        assertEquals("Successful", response.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnNotFoundWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());

        UserDTO response = userService.getAllUsers();

        assertEquals(404, response.getStatusCode());
        assertEquals("No users found", response.getMessage());
    }

    @Test
    void getUsersById_shouldReturnUserSuccessfully() {
        SystemUser user = new SystemUser();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDTO response = userService.getUsersById(1);

        assertEquals(200, response.getStatusCode());
        assertEquals("Users with id '1' found successfully", response.getMessage());
    }

    @Test
    void getUsersById_shouldReturnErrorWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO response = userService.getUsersById(1);

        assertEquals(500, response.getStatusCode());
    }

    @Test
    void deleteUser_shouldDeleteUserSuccessfully() {
        SystemUser user = new SystemUser();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDTO response = userService.deleteUser(1);

        assertEquals(200, response.getStatusCode());
        assertEquals("User deleted successfully", response.getMessage());
    }

    @Test
    void deleteUser_shouldReturnErrorWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO response = userService.deleteUser(1);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found for deletion", response.getMessage());
    }

    @Test
    void updateUser_shouldUpdateUserSuccessfully() {
        SystemUser user = new SystemUser();
        user.setId(1);

        UserDTO updatedUser = new UserDTO();
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(SystemUser.class))).thenReturn(user);

        UserDTO response = userService.updateUser(1, updatedUser);

        assertEquals(200, response.getStatusCode());
        assertEquals("User updated successfully", response.getMessage());
    }

    @Test
    void updateUser_shouldReturnErrorWhenUserNotFound() {
        UserDTO updatedUser = new UserDTO();

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO response = userService.updateUser(1, updatedUser);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found for update", response.getMessage());
    }

    @Test
    void getMyInfo_shouldReturnUserInfoSuccessfully() {
        SystemUser user = new SystemUser();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDTO response = userService.getMyInfo("test@example.com");

        assertEquals(200, response.getStatusCode());
        assertEquals("successful", response.getMessage());
    }

    @Test
    void getMyInfo_shouldReturnErrorWhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        UserDTO response = userService.getMyInfo("test@example.com");

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found for update", response.getMessage());
    }
}