package com.Jkcards.jk_user.services;

import com.Jkcards.jk_user.dtos.UserRequestDto;
import com.Jkcards.jk_user.dtos.UserResponseDto;
import com.Jkcards.jk_user.entities.Role;
import com.Jkcards.jk_user.entities.User;
import com.Jkcards.jk_user.repositories.RoleRepository;
import com.Jkcards.jk_user.repositories.UserRepository;
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
        Page<User> result = repository.findAll(pageable);
        return result.map(x-> new UserResponseDto(x));
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = repository.findById(id).get();
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto insert(UserRequestDto dto) {

        User user = new User();
        copyDtoToEntity(user,dto);
        user.setActive(true);
        user = repository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto update(UserRequestDto requestDto, Long id) {

        User user = repository.findById(id).get();

        copyDtoToEntityForUpdate(user,requestDto);
        user.setActive(true);
        user = repository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long id) {

        User user = repository.findById(id).get();
        user.setActive(false);
    }

    private void copyDtoToEntity(User user, UserRequestDto dto) {

        user.setName(dto.getName());
        user.setCellPhone(dto.getCellPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByRoleName("CLIENT");
        user.setRole(role);
    }

    private void copyDtoToEntityForUpdate(User user,UserRequestDto requestDto) {

        user.setName(requestDto.getName());
        user.setCellPhone(requestDto.getCellPhone());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByRoleName(requestDto.getRoleName());
        user.setRole(role);
    }
}
