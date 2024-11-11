package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.UserDTO;
import com.hotel.horizonstay.exception.UserNotFoundException;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.helper.FileUploadUtil;
import com.hotel.horizonstay.helper.Validation;
import com.hotel.horizonstay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private final Validation validation = new Validation();
    private final ErrorResponse error = new ErrorResponse();

    @PostMapping(value = "/auth/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> register(@RequestParam("files") MultipartFile[] files, @RequestPart("user") UserDTO reg)
    {

        // Validate the user input
        if (reg == null)
        {
            return error.createErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }

        if (validation.isInvalidUserData(reg))
        {
            return error.createErrorResponse("Invalid user data", HttpStatus.BAD_REQUEST);
        }

        // Directory where the files will be stored
        String uploadDir = "/profileImages/";

        try
        {

            // Save each file and get the filenames
            List<String> fileNames = Arrays.stream(files).map(file ->
                    {
                        try
                        {

                            if (file.getOriginalFilename() == null)
                            {
                                throw new RuntimeException("File upload error");
                            }

                            // Get a clean file name
                            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                            // Save the file to the upload directory
                            FileUploadUtil.saveFile(uploadDir, fileName, file);

                            return fileName;

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values if any file failed to save
                    .toList();

            reg.setImage(fileNames.get(0)); // Set the first image as the profile image

            // Register the user using the service layer
            UserDTO response = userService.register(reg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while registering user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint for user login
    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO)
    {
        if (userDTO == null)
        {
            return error.createErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty() || userDTO.getPassword() == null || userDTO.getPassword().isEmpty())
        {
            return error.createErrorResponse("Email or password is missing", HttpStatus.BAD_REQUEST);
        }

        try
        {
            UserDTO response = userService.login(userDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while logging in", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for refreshing user token
    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refreshToken(@RequestBody UserDTO userDTO)
    {
        if (userDTO == null)
        {
            return error.createErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }

        try
        {
            UserDTO response = userService.refreshToken(userDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while refreshing token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for fetching all users (admin only)
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUsers()
    {
        try
        {
            UserDTO response = userService.getAllUsers();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while fetching all users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId)
    {
        try
        {
            UserDTO response = userService.getUsersById(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            return error.createErrorResponse("User not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while fetching user by ID", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for updating a user (admin only)
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @RequestBody UserDTO user)
    {
//        if (user == null)
//        {
//            return error.createErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
//        }
//        if (validation.isInvalidUserData(user))
//        {
//            return error.createErrorResponse("Invalid user data", HttpStatus.BAD_REQUEST);
//        }

        try
        {
            UserDTO response = userService.updateUser(userId, user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Integer userId)
    {
        try
        {
            UserDTO response = userService.deleteUser(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            return error.createErrorResponse("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e)
        {
            return error.createErrorResponse("Error occurred while deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<UserDTO> getMyProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null)
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();

        UserDTO userDTO = userService.getMyInfo(email);

        if (userDTO == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}