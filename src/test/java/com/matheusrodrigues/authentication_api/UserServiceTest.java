package com.matheusrodrigues.authentication_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.matheusrodrigues.authentication_api.dto.LoginReturn;
import com.matheusrodrigues.authentication_api.enums.RoleName;
import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.repository.UserRepository;
import com.matheusrodrigues.authentication_api.service.JwtService;
import com.matheusrodrigues.authentication_api.service.UserService;
import com.matheusrodrigues.authentication_api.models.Role;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Mock
    private JwtService jwt;

    @Mock
    private BCryptPasswordEncoder bcrypt;

    private Role roleAdmin;
    private User admin;

    @BeforeEach
    @DisplayName("método transcrito(chat-gpt) para mockar adme e seu token)")
    public void before() {

        roleAdmin = new Role();
        roleAdmin.setName(RoleName.ROLE_ADMIN);

        admin = new User("Admin", "admin@gmail.com", "admin123");
        admin.setRole(roleAdmin);

    }

    @Test
    @DisplayName("should save user with sucess")
    public void saveUserTest() {

        User user = new User("Matheus", "matheusluizroza@gmail.com", "140208");

        when(repository.save(user)).thenReturn(user);

        User userTest = service.saveUser(user);

        assertNotNull(userTest);
        assertEquals("Matheus", userTest.getUsername());
        assertEquals("matheusluizroza@gmail.com", userTest.getEmail());
        assertEquals("140208", userTest.getPassword());

        verify(repository).save(user);
    }

    @Test
    @DisplayName("should login in account with sucess")
    public void loginByEmailTest() {

        User user = new User("Matheus", "matheusluizroza@gmail.com", "140208");

        when(repository.findByEmail("matheusluizroza@gmail.com")).thenReturn(Optional.of(user));

        LoginReturn userTest = service.login("matheusluizroza@gmail.com", "140208");

        assertNotNull(userTest);
        assertEquals(user.getEmail(), userTest.getUser().getEmail());

        verify(repository).findByEmail("matheusluizroza@gmail.com");

    }

    @Test
    @DisplayName("should return all users")
    public void getAllUsersTest() {

        User user1 = new User("Matheus", "matheusluizroza@gmail.com", "140208");
        User user2 = new User("Miguel", "miguelluizroza@gmail.com", "140208");
        User user3 = new User("Eloá", "eloaa@gmail.com", "140208");

        List<User> listUsers = List.of(user1, user2, user3);

        when(repository.findAll()).thenReturn(listUsers);

        List<User> listUsersTest = service.getAllUsers();

        assertNotNull(listUsersTest);
        assertEquals(listUsers, listUsersTest);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("should set user's role with success")
    public void setRoleUserTest() {

        when(jwt.getUserFromToken("abcd", repository)).thenReturn(admin);

        Role newRole = new Role();
        newRole.setName(RoleName.ROLE_MANAGER);

        User userToChange = new User("Matheus", "matheus@gmail.com", "123456");
        userToChange.setId(1);
        userToChange.setRole(roleAdmin);

        when(repository.findById(userToChange.getId())).thenReturn(Optional.of(userToChange));
        when(repository.save(userToChange)).thenReturn(userToChange);

        User updatedUser = service.setRoleUser(userToChange.getId(), newRole);

        assertNotNull(updatedUser);
        assertEquals(newRole.getName(), updatedUser.getRole().getName());

        verify(repository).save(userToChange);
    }

    @Test
    @DisplayName("should set user with sucess")
    public void updateUserTest() {

        when(jwt.getUserFromToken("abcd", repository)).thenReturn(admin);
        when(repository.findById(admin.getId())).thenReturn(Optional.of(admin));

        User newAdmin = new User("MatheusAdmin", "matheusluizroza@gmail.com", "admin123");
        User adminUpdated = service.updateUser("abcd", admin.getId(), newAdmin);

        assertNotNull(adminUpdated);
        assertEquals("MatheusAdmin", adminUpdated.getUsername());
        assertEquals("matheusluizroza@gmail.com", adminUpdated.getEmail());
        verify(repository).findById(admin.getId());

    }

    @Test
    @DisplayName("should get user by id with sucess")
    public void getUserById() {

        when(repository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(jwt.getUserFromToken("abcd", repository)).thenReturn(admin);

        User thatUser = service.getUserById(admin.getId(), "abcd");

        assertNotNull(thatUser);
        assertEquals(0, thatUser.getId());
        verify(repository).findById(admin.getId());

    }

    @Test
    @DisplayName("should delete user with sucess")
    public void deleteUser() {
        User user = new User("Matheus", "matheusLuiz", "140208");

        when(jwt.getUserFromToken("abcd", repository)).thenReturn(admin);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        User userDeleted = service.deleteUser(user.getId(), "abcd");

        assertNotNull(userDeleted);
        assertEquals(user.getId(), userDeleted.getId());
        verify(repository).deleteById(user.getId());
    }

    @Test
    @DisplayName("should get all users by that Role")
    public void getUsersByRole() {
        User user1 = new User("Matheus", "matheusluizroza@gmail.com", "140208");
        User user2 = new User("Miguel", "miguelluizroza@gmail.com", "140208");
        User user3 = new User("Eloá", "eloaa@gmail.com", "140208");

        Role admin = new Role();
        admin.setName(RoleName.ROLE_ADMIN);
        Role manager = new Role();
        manager.setName(RoleName.ROLE_MANAGER);

        user1.setRole(manager);
        user2.setRole(manager);
        user3.setRole(admin);

        List<User> listUsers = List.of(user1, user2, user3);

        when(repository.findAll()).thenReturn(listUsers);

        List<User> listUsersByRole = service.getUsersByRole(manager);

        assertNotNull(listUsersByRole);
        assertEquals(user1.getUsername(), listUsersByRole.get(0).getUsername());
        assertEquals(user2.getUsername(), listUsersByRole.get(1).getUsername());
        verify(repository).findAll();

    }

    @Test
    @DisplayName("should return totality users with that role")
    public void countUsersByRole() {
        User user1 = new User("Matheus", "matheusluizroza@gmail.com", "140208");
        User user2 = new User("Miguel", "miguelluizroza@gmail.com", "140208");
        User user3 = new User("Eloá", "eloaa@gmail.com", "140208");

        Role admin = new Role();
        admin.setName(RoleName.ROLE_ADMIN);
        Role manager = new Role();
        manager.setName(RoleName.ROLE_MANAGER);

        user1.setRole(manager);
        user2.setRole(manager);
        user3.setRole(admin);

        List<User> userList = List.of(user1, user2, user3);

        when(repository.countByRole(manager)).thenReturn(2);

        Integer countList = service.countUsersByRole(manager);

        assertNotNull(countList);
        assertEquals(2, countList);
        verify(repository).countByRole(manager);

    }

}
