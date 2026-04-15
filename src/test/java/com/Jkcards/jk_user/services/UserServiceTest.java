package com.Jkcards.jk_user.services;

import com.Jkcards.jk_user.dtos.UserRequestDto;
import com.Jkcards.jk_user.dtos.UserResponseDto;
import com.Jkcards.jk_user.entities.Role;
import com.Jkcards.jk_user.entities.User;
import com.Jkcards.jk_user.repositories.RoleRepository;
import com.Jkcards.jk_user.repositories.UserRepository;
import com.Jkcards.jk_user.services.exceptions.DatabaseException;
import com.Jkcards.jk_user.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRequestDto userRequestDto;
    private User user;
    private Role role;
    private Long existsId, nonExistsId;

    @BeforeEach
    void SetUP(){

        existsId = 1L;
        nonExistsId = 200L;

        role = new Role();
        role.setId(1L);
        role.setRoleName("CLIENT");

        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Maria");
        userRequestDto.setEmail("maria@gmail.com");
        userRequestDto.setPassword("123456");
        userRequestDto.setCellPhone("15988233584");
        userRequestDto.setRoleName("CLIENT");

        user = new User();
        user.setId(3L);
        user.setName("Maria");
        user.setEmail("maria@gmail.com");
        user.setRole(role);


    }

    @Test
    void findByIdShouldReturnUserResponseDtoWhenIdExist(){

        when(userRepository.findById(existsId)).thenReturn(Optional.of(user));
        UserResponseDto result = userService.findById(existsId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Maria", result.getName());
        Assertions.assertEquals("maria@gmail.com", result.getEmail());

        verify(userRepository, times(1)).findById(existsId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        when(userRepository.findById(nonExistsId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
            userService.findById(nonExistsId);
        });

        verify(userRepository, times(1)).findById(nonExistsId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAllShouldReturnPageOfUserResponseDto() {

        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findByActiveTrue(pageable)).thenReturn(page);

        Page<UserResponseDto> result = userService.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Maria", result.getContent().get(0).getName());
        Assertions.assertEquals("maria@gmail.com", result.getContent().get(0).getEmail());

        verify(userRepository, times(1)).findByActiveTrue(pageable);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAllShouldReturnEmptyPageWhenNoData() {

        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<User> emptyPage = new PageImpl<>(List.of());

        when(userRepository.findByActiveTrue(pageable)).thenReturn(emptyPage);

        Page<UserResponseDto> result = userService.findAll(pageable);

        Assertions.assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByActiveTrue(pageable);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void insertShouldReturnUserResponseDtoWhenEmailDoesNotExist(){

        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName("CLIENT")).thenReturn(role);
        when(passwordEncoder.encode("123456")).thenReturn("Encrypted password");
        when(userRepository.save(any())).thenReturn(user);

        var result = userService.insert(userRequestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Maria", result.getName());
        Assertions.assertEquals("maria@gmail.com", result.getEmail());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        Assertions.assertTrue(saved.getActive());

        verify(roleRepository).findByRoleName("CLIENT");
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void InsertShouldThrowsDatabaseExceptionWhenEmailExist(){

        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, ()->{
            userService.insert(userRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateShouldReturnUserResponseDtoWhenDataIsValid(){

        when(userRepository.findById(existsId)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName("CLIENT")).thenReturn(role);
        when(userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), existsId)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        var result = userService.update(userRequestDto, existsId);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        Assertions.assertEquals("Maria", saved.getName());
        Assertions.assertEquals("maria@gmail.com", saved.getEmail());
        Assertions.assertTrue(saved.getActive());

        verify(userRepository).findById(existsId);
        verify(userRepository).existsByEmailAndIdNot(userRequestDto.getEmail(), existsId);
    }

    @Test
    void updateShouldThrowDatabaseExceptionWhenEmailExist(){


        when(userRepository.findById(existsId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), existsId)).thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, ()->{
            userService.update(userRequestDto, existsId);
        });

        verify(userRepository, never()).save(any());

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        when(userRepository.findById(nonExistsId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            userService.update(userRequestDto, nonExistsId);
        });

        verify(userRepository, never()).save(any());
        verify(userRepository, never()).existsByEmailAndIdNot(any(), any());
    }

    @Test
    void deleteShouldSoftDeleteWhenIdExists() {

        when(userRepository.findById(existsId)).thenReturn(Optional.of(user));

        userService.delete(existsId);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        Assertions.assertFalse(saved.getActive());

        verify(userRepository).findById(existsId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        when(userRepository.findById(nonExistsId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
           userService.delete(nonExistsId);
        });

        verify(userRepository).findById(nonExistsId);
        verify(userRepository, never()).save(any());
    }


}
