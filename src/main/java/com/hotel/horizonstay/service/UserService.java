package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.UserDTO;
import com.hotel.horizonstay.entity.SystemUser;
import com.hotel.horizonstay.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserDTO register(UserDTO registrationRequest)
    {
        logger.info("Registering user with email: {}", registrationRequest.getEmail());
        UserDTO reqRes = new UserDTO();

        try {

            // Check if user already exists with the same email
            Optional<SystemUser> existingUser = usersRepo.findByEmail(registrationRequest.getEmail());

            if (existingUser.isPresent())
            {
                logger.warn("User already exists with email: {}", registrationRequest.getEmail());
                reqRes.setMessage("User already exists");
                reqRes.setStatusCode(409); // Conflict
                return reqRes;
            }

            SystemUser systemUser = new SystemUser();
            systemUser.setEmail(registrationRequest.getEmail());
            systemUser.setName(registrationRequest.getName());
            systemUser.setRole(registrationRequest.getRole());
            systemUser.setAddress(registrationRequest.getAddress());
            systemUser.setImage(registrationRequest.getImage());
            systemUser.setNIC(registrationRequest.getNIC());
            systemUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            SystemUser ourUsersResult = usersRepo.save(systemUser);

            if (ourUsersResult.getId() > 0)
            {
                reqRes.setSystemUsers(ourUsersResult);
                reqRes.setMessage("User Saved Successfully");
                reqRes.setStatusCode(200);
                logger.info("User registered successfully with email: {}", registrationRequest.getEmail());
            }

        } catch (Exception e) {
            logger.error("Error occurred while registering user: {}", e.getMessage());
            reqRes.setStatusCode(500);
            reqRes.setError(e.getMessage());
        }

        return reqRes;
    }

    public UserDTO login(UserDTO loginRequest)
    {
        logger.info("Logging in user with email: {}", loginRequest.getEmail());
        UserDTO reqRes = new UserDTO();

        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            reqRes.setStatusCode(200);
            reqRes.setToken(jwt);
            reqRes.setUserId(user.getId());
            reqRes.setRole(user.getRole());
            reqRes.setImage(user.getImage());
            reqRes.setEmail(user.getEmail());
            reqRes.setName(user.getName());
            reqRes.setAddress(user.getAddress());
            reqRes.setRefreshToken(refreshToken);
            reqRes.setExpirationTime("24Hrs");
            reqRes.setMessage("Successfully Logged In");
            logger.info("User logged in successfully with email: {}", loginRequest.getEmail());

        } catch (Exception e)
        {
            logger.error("Error occurred while logging in user: {}", e.getMessage());
            reqRes.setStatusCode(500);
            reqRes.setError(e.getMessage());
        }

        return reqRes;
    }

    public UserDTO refreshToken(UserDTO refreshTokenRequest)
    {
        logger.info("Refreshing token for user with email: {}", refreshTokenRequest.getEmail());
        UserDTO res = new UserDTO();

        try
        {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            SystemUser users = usersRepo.findByEmail(ourEmail).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users))
            {
                var jwt = jwtUtils.generateToken(users);
                res.setStatusCode(200);
                res.setToken(jwt);
                res.setRefreshToken(refreshTokenRequest.getToken());
                res.setExpirationTime("24Hr");
                res.setMessage("Successfully Refreshed Token");
                logger.info("Token refreshed successfully for user with email: {}", ourEmail);
            }

            res.setStatusCode(200);
            return res;

        } catch (Exception e)
        {
            logger.error("Error occurred while refreshing token: {}", e.getMessage());
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
            return res;
        }
    }


//    @Cacheable(value = "users")
    public UserDTO getAllUsers()
    {
        logger.info("Fetching all users");
        UserDTO res = new UserDTO();

        try
        {

            List<SystemUser> result = usersRepo.findAll();

            if (!result.isEmpty())
            {
                res.setSystemUsersList(result);
                res.setStatusCode(200);
                res.setMessage("Successful");
                logger.info("Fetched all users successfully");
            } else
            {
                res.setStatusCode(404);
                res.setMessage("No users found");
                logger.warn("No users found");
            }
            return res;

        } catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            return res;
        }
    }


//    @Cacheable(value = "users", key = "#id")
    public UserDTO getUsersById(Integer id)
    {
        logger.info("Fetching user with id: {}", id);
        UserDTO res = new UserDTO();

        try
        {

            SystemUser usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            res.setSystemUsers(usersById);
            res.setStatusCode(200);
            res.setMessage("Users with id '" + id + "' found successfully");
            logger.info("User with id: {} found successfully", id);

        } catch (Exception e)
        {
            logger.error("Error occurred while fetching user with id: {}", id, e);
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());

        }
        return res;
    }

//    @CacheEvict(value = "users", key = "#userId")
    public UserDTO deleteUser(Integer userId)
    {
        logger.info("Deleting user with id: {}", userId);
        UserDTO res = new UserDTO();

        try
        {
            Optional<SystemUser> userOptional = usersRepo.findById(userId);

            if (userOptional.isPresent())
            {
                usersRepo.deleteById(userId);
                res.setStatusCode(200);
                res.setMessage("User deleted successfully");
                logger.info("User with id: {} deleted successfully", userId);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("User not found for deletion");
                logger.warn("User with id: {} not found for deletion", userId);

            }

        } catch (Exception e)
        {
            logger.error("Error occurred while deleting user with id: {}", userId, e);
            res.setStatusCode(500);
            res.setMessage("Error occurred while deleting user: " + e.getMessage());

        }
        return res;
    }

//    @CachePut(value = "users", key = "#userId")
    public UserDTO updateUser(Integer userId, UserDTO updatedUser)
    {
        logger.info("Updating user with id: {}", userId);
        UserDTO res = new UserDTO();

        try
        {
            Optional<SystemUser> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent())
            {
                SystemUser existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setImage(updatedUser.getImage());
                existingUser.setNIC(updatedUser.getNIC());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty())
                {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                SystemUser savedUser = usersRepo.save(existingUser);
                res.setSystemUsers(savedUser);
                res.setStatusCode(200);
                res.setMessage("User updated successfully");
                logger.info("User with id: {} updated successfully", userId);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("User not found for update");
                logger.warn("User with id: {} not found for update", userId);

            }
        } catch (Exception e)
        {
            logger.error("Error occurred while updating user with id: {}", userId, e);
            res.setStatusCode(500);
            res.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return res;
    }

//    @Cacheable(value = "users", key = "#email")
    public UserDTO getMyInfo(String email)
    {
        logger.info("Fetching info for user with email: {}", email);
        UserDTO res = new UserDTO();

        try
        {
            Optional<SystemUser> userOptional = usersRepo.findByEmail(email);

            if (userOptional.isPresent())
            {
                res.setSystemUsers(userOptional.get());
                res.setStatusCode(200);
                res.setMessage("successful");
                logger.info("Fetched info for user with email: {} successfully", email);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("User not found for update");
                logger.warn("User with email: {} not found for update", email);

            }

        } catch (Exception e) {
            logger.error("Error occurred while getting user info for email: {}", email, e);
            res.setStatusCode(500);
            res.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return res;

    }
}
