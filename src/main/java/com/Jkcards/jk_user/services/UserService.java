package com.Jkcards.jk_user.services;

import com.Jkcards.jk_user.dtos.UserRequestDto;
import com.Jkcards.jk_user.dtos.UserResponseDto;
import com.Jkcards.jk_user.entities.Role;
import com.Jkcards.jk_user.entities.User;
import com.Jkcards.jk_user.repositories.RoleRepository;
import com.Jkcards.jk_user.repositories.UserRepository;
import com.Jkcards.jk_user.services.exceptions.DatabaseException;
import com.Jkcards.jk_user.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        Page<User> result = repository.findByActiveTrue(pageable);
        return result.map(x-> new UserResponseDto(x));
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {

        User user = repository.findById(id).orElseThrow(()->new  ResourceNotFoundException("User Not Found"));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto insert(UserRequestDto dto) {

        if (repository.existsByEmail(dto.getEmail())){
            throw new DatabaseException("Email already exists");
        }

        User user = new User();
        copyDtoToEntity(user,dto);
        user.setActive(true);
        user = repository.save(user);
        return new UserResponseDto(user);
    }


    @Transactional
    public UserResponseDto update(UserRequestDto requestDto, Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new DatabaseException("Email already exists");
        }

        copyDtoToEntityForUpdate(user, requestDto);
        user = repository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long id) {

        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        user = repository.save(user);
    }

    private void copyDtoToEntity(User user, UserRequestDto dto) {

        user.setName(dto.getName());
        user.setCellPhone(dto.getCellPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.findByRoleName("CLIENT");

        if (role == null){
            throw new ResourceNotFoundException("Role not found");
        }
        user.setRole(role);
    }

    private void copyDtoToEntityForUpdate(User user,UserRequestDto requestDto) {

        user.setName(requestDto.getName());
        user.setCellPhone(requestDto.getCellPhone());
        user.setEmail(requestDto.getEmail());

        if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        Role role = roleRepository.findByRoleName("CLIENT");

        if (role == null){
            throw new ResourceNotFoundException("Role not found");
        }
        user.setRole(role);
    }
}
