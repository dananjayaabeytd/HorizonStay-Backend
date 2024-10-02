//package com.hotel.horizonstay.controller;
//
//import com.hotel.horizonstay.dto.UserDTO;
//import com.hotel.horizonstay.exception.UserNotFoundException;
//import com.hotel.horizonstay.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//
//class UserControllerTest
//{
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp()
//    {
//        // Initialize mocks before each test
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegister_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("test@example.com");
//        userDTO.setAddress("USA");
//        userDTO.setPassword("test");
//        userDTO.setRole("ADMIN");
//        userDTO.setImage("img.jpg");
//        userDTO.setNIC("testNIC");
//
//        // Mock the register method of userService to return the sample UserDTO
//        when(userService.register(any(UserDTO.class))).thenReturn(userDTO);
//
//        // Call the register method of userController
//        ResponseEntity<UserDTO> response = userController.register(userDTO);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testRegister_NullRequest()
//    {
//        // Call the register method with null request body
//        ResponseEntity<UserDTO> response = userController.register(null);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testRegister_InvalidData()
//    {
//        // Create a UserDTO object with invalid data
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("");
//
//        // Call the register method with invalid data
//        ResponseEntity<UserDTO> response = userController.register(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid user data", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testRegister_Exception()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("test@example.com");
//        userDTO.setAddress("USA");
//        userDTO.setPassword("test");
//        userDTO.setRole("ADMIN");
//        userDTO.setImage("img.jpg");
//        userDTO.setNIC("testNIC");
//
//        // Mock the register method of userService to throw an exception
//        when(userService.register(any(UserDTO.class))).thenThrow(new RuntimeException("Error Registering user"));
//
//        // Call the register method of userController
//        ResponseEntity<UserDTO> response = userController.register(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while registering user", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testLogin_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("test");
//
//        // Mock the login method of userService to return the sample UserDTO
//        when(userService.login(any(UserDTO.class))).thenReturn(userDTO);
//
//        // Call the login method of userController
//        ResponseEntity<UserDTO> response = userController.login(userDTO);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testLogin_NullRequest()
//    {
//        // Call the login method with null request body
//        ResponseEntity<UserDTO> response = userController.login(null);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testLogin_MissingFields()
//    {
//        // Create a UserDTO object with missing email
//        UserDTO userDTO = new UserDTO();
//        userDTO.setPassword("test");
//
//        // Call the login method with missing email
//        ResponseEntity<UserDTO> response = userController.login(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Email or password is missing", Objects.requireNonNull(response.getBody()).getMessage());
//
//        // Create a UserDTO object with missing password
//        userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Call the login method with missing password
//        response = userController.login(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Email or password is missing", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testLogin_Exception()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("test");
//
//        // Mock the login method of userService to throw an exception
//        when(userService.login(any(UserDTO.class))).thenThrow(new RuntimeException("Error Logging in"));
//
//        // Call the login method of userController
//        ResponseEntity<UserDTO> response = userController.login(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while logging in", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testRefreshToken_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Mock the refreshToken method of userService to return the sample UserDTO
//        when(userService.refreshToken(any(UserDTO.class))).thenReturn(userDTO);
//
//        // Call the refreshToken method of userController
//        ResponseEntity<UserDTO> response = userController.refreshToken(userDTO);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testRefreshToken_NullRequest()
//    {
//        // Call the refreshToken method with null request body
//        ResponseEntity<UserDTO> response = userController.refreshToken(null);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testRefreshToken_Exception()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Mock the refreshToken method of userService to throw an exception
//        when(userService.refreshToken(any(UserDTO.class))).thenThrow(new RuntimeException("Error Refreshing token"));
//
//        // Call the refreshToken method of userController
//        ResponseEntity<UserDTO> response = userController.refreshToken(userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while refreshing token", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testGetAllUsers_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Mock the getAllUsers method of userService to return the sample UserDTO
//        when(userService.getAllUsers()).thenReturn(userDTO);
//
//        // Call the getAllUsers method of userController
//        ResponseEntity<UserDTO> response = userController.getAllUsers();
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testGetAllUsers_Exception()
//    {
//        // Mock the getAllUsers method of userService to throw an exception
//        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error Fetching all users"));
//
//        // Call the getAllUsers method of userController
//        ResponseEntity<UserDTO> response = userController.getAllUsers();
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while fetching all users", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testGetUserById_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Mock the getUsersById method of userService to return the sample UserDTO
//        when(userService.getUsersById(anyInt())).thenReturn(userDTO);
//
//        // Call the getUserById method of userController
//        ResponseEntity<UserDTO> response = userController.getUserById(1);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testGetUserById_NotFound()
//    {
//        // Mock the getUsersById method of userService to throw a UserNotFoundException
//        when(userService.getUsersById(anyInt())).thenThrow(new UserNotFoundException("User Not found"));
//
//        // Call the getUserById method of userController
//        ResponseEntity<UserDTO> response = userController.getUserById(1);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("User not found", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testGetUserById_Exception()
//    {
//        // Mock the getUsersById method of userService to throw an exception
//        when(userService.getUsersById(anyInt())).thenThrow(new RuntimeException("Error Fetching user by ID"));
//
//        // Call the getUserById method of userController
//        ResponseEntity<UserDTO> response = userController.getUserById(1);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while fetching user by ID", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testUpdateUser_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("test@example.com");
//        userDTO.setAddress("USA");
//        userDTO.setPassword("test");
//        userDTO.setRole("ADMIN");
//        userDTO.setImage("img.jpg");
//        userDTO.setNIC("testNIC");
//
//        // Mock the updateUser method of userService to return the sample UserDTO
//        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenReturn(userDTO);
//
//        // Call the updateUser method of userController
//        ResponseEntity<UserDTO> response = userController.updateUser(1, userDTO);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testUpdateUser_NullRequest()
//    {
//        // Call the updateUser method with null request body
//        ResponseEntity<UserDTO> response = userController.updateUser(1, null);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testUpdateUser_InvalidData()
//    {
//        // Create a UserDTO object with invalid data
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName(""); // Invalid data
//
//        // Call the updateUser method with invalid data
//        ResponseEntity<UserDTO> response = userController.updateUser(1, userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid user data", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testUpdateUser_Exception()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("test@example.com");
//        userDTO.setAddress("USA");
//        userDTO.setPassword("test");
//        userDTO.setRole("ADMIN");
//        userDTO.setImage("img.jpg");
//        userDTO.setNIC("testNIC");
//
//        // Mock the updateUser method of userService to throw an exception
//        when(userService.updateUser(anyInt(), any(UserDTO.class))).thenThrow(new RuntimeException("Error Updating user"));
//
//        // Call the updateUser method of userController
//        ResponseEntity<UserDTO> response = userController.updateUser(1, userDTO);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while updating user", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testDeleteUser_Success()
//    {
//        // Create a sample UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//
//        // Mock the deleteUser method of userService to return the sample UserDTO
//        when(userService.deleteUser(anyInt())).thenReturn(userDTO);
//
//        // Call the deleteUser method of userController
//        ResponseEntity<UserDTO> response = userController.deleteUser(1);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("test@example.com", Objects.requireNonNull(response.getBody()).getEmail());
//    }
//
//    @Test
//    void testDeleteUser_NotFound()
//    {
//        // Mock the deleteUser method of userService to throw a UserNotFoundException
//        when(userService.deleteUser(anyInt())).thenThrow(new UserNotFoundException("User not found"));
//
//        // Call the deleteUser method of userController
//        ResponseEntity<UserDTO> response = userController.deleteUser(1);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("User not found", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//
//    @Test
//    void testDeleteUser_Exception()
//    {
//        // Mock the deleteUser method of userService to throw an exception
//        when(userService.deleteUser(anyInt())).thenThrow(new RuntimeException("Error Deleting user"));
//
//        // Call the deleteUser method of userController
//        ResponseEntity<UserDTO> response = userController.deleteUser(1);
//
//        // Assert the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while deleting user", Objects.requireNonNull(response.getBody()).getMessage());
//    }
//}