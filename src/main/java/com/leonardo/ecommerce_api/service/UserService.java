package com.leonardo.ecommerce_api.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.leonardo.ecommerce_api.dto.LoginRequestDTO;
import com.leonardo.ecommerce_api.dto.UserRequestDTO;
import com.leonardo.ecommerce_api.dto.UserResponseDTO;
import com.leonardo.ecommerce_api.model.Role;
import com.leonardo.ecommerce_api.model.User;
import com.leonardo.ecommerce_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        return toResponseDTO(saved);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserResponseDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toResponseDTO);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullName(dto.getFullName());
                    user.setEmail(dto.getEmail());
                    if (dto.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }
                    user.setUpdatedAt(OffsetDateTime.now());
                    User updated = userRepository.save(user);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public UserResponseDTO validateLogin(LoginRequestDTO dto) {
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            return toResponseDTO(userOpt.get());
        } else if (userOpt.isPresent()) {
            throw new RuntimeException("A senha inserida não coincide com a cadastrada!");
        }
        throw new RuntimeException("O usuário com este e-mail não foi encontrado!");
    }
}