package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.UserDTO;
import com.hotel.horizonstay.exception.UserNotFoundException;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.helper.Validation;
import com.hotel.horizonstay.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Validation validation;

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_NullRequestBody()
    {
        MultipartFile[] files = {};

        ResponseEntity<UserDTO> response = userController.register(files, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterUser_InvalidUserData()
    {
        MultipartFile[] files = {};
        UserDTO userDTO = new UserDTO();

        when(validation.isInvalidUserData(any(UserDTO.class))).thenReturn(true);

        ResponseEntity<UserDTO> response = userController.register(files, userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testLoginUser_Success()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        when(userService.login(any(UserDTO.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.login(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testLoginUser_NullRequestBody()
    {
        ResponseEntity<UserDTO> response = userController.login(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testLoginUser_MissingEmailOrPassword()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        ResponseEntity<UserDTO> response = userController.login(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testLoginUser_InternalServerError()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        when(userService.login(any(UserDTO.class))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.login(userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRefreshToken_Success()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        when(userService.refreshToken(any(UserDTO.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.refreshToken(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testRefreshToken_NullRequestBody()
    {
        ResponseEntity<UserDTO> response = userController.refreshToken(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRefreshToken_InternalServerError()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        when(userService.refreshToken(any(UserDTO.class))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.refreshToken(userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllUsers_Success()
    {
        UserDTO userDTO = new UserDTO();

        when(userService.getAllUsers()).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testGetAllUsers_InternalServerError()
    {
        when(userService.getAllUsers()).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetUserById_Success()
    {
        UserDTO userDTO = new UserDTO();

        when(userService.getUsersById(any(Integer.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testGetUserById_UserNotFound()
    {
        when(userService.getUsersById(any(Integer.class))).thenThrow(new UserNotFoundException("User Not found"));

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUserById_InternalServerError()
    {
        when(userService.getUsersById(any(Integer.class))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateUser_Success()
    {
        UserDTO userDTO = new UserDTO();

        when(userService.updateUser(any(Integer.class), any(UserDTO.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testUpdateUser_InternalServerError()
    {
        UserDTO userDTO = new UserDTO();

        when(userService.updateUser(any(Integer.class), any(UserDTO.class))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.updateUser(1, userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDeleteUser_Success()
    {
        UserDTO userDTO = new UserDTO();

        when(userService.deleteUser(any(Integer.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.deleteUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testDeleteUser_UserNotFound()
    {
        when(userService.deleteUser(any(Integer.class))).thenThrow(new UserNotFoundException("User Not found"));

        ResponseEntity<UserDTO> response = userController.deleteUser(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUser_InternalServerError()
    {
        when(userService.deleteUser(any(Integer.class))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.deleteUser(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetMyProfile_Success()
    {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userService.getMyInfo(any(String.class))).thenReturn(userDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getMyProfile();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }



//    @Test
//    void testRegisterUser_Success() throws Exception {
//        MultipartFile[] files = {};
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        when(validation.isInvalidUserData(any(UserDTO.class))).thenReturn(false);
//        when(userService.register(any(UserDTO.class))).thenReturn(userDTO);
//
//        ResponseEntity<UserDTO> response = userController.register(files, userDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(userDTO, response.getBody());
//    }


//    @Test
//    void testGetMyProfile_Success() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        when(userService.getMyInfo(any(String.class))).thenReturn(userDTO);
//
//        ResponseEntity<UserDTO> response = userController.getMyProfile();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(userDTO, response.getBody());
//    }

    // UserControllerTest.java
//    @Test
//    void testRegisterUser_FileUploadError() {
//        MultipartFile[] files = {new MockMultipartFile("file", (byte[]) null)};
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        when(validation.isInvalidUserData(any(UserDTO.class))).thenReturn(false);
//
//        ResponseEntity<UserDTO> response = userController.register(files, userDTO);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
}