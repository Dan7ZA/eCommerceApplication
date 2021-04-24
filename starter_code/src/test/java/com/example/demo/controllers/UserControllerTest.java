package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private User userMock = mock(User.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser() throws Exception {

        when(encoder.encode("password")).thenReturn("thisIsHashed");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Dan");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("password");

        final ResponseEntity<User> responseEntity = userController.createUser(userRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("Dan", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void findUserById() throws Exception {
        User userMock = mock(User.class);
        userMock.setId(1L);
        userMock.setUsername("DanMockUser");
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(userMock));

        final ResponseEntity<User> responseEntity = userController.findById(1L);
        User user = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(user);
        assertEquals(userMock.getId(), user.getId());
        assertEquals(userMock.getUsername(), user.getUsername());
    }

    @Test
    public void findUserByUserName() throws Exception {
        User userMock = mock(User.class);
        userMock.setId(1L);
        userMock.setUsername("DanMockUser");
        when(userRepo.findByUsername("DanMockUser")).thenReturn(userMock);

        final ResponseEntity<User> responseEntity = userController.findByUserName("DanMockUser");
        User user = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(user);
        assertEquals(userMock.getId(), user.getId());
        assertEquals(userMock.getUsername(), user.getUsername());
    }



}
