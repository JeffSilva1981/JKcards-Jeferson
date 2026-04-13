package com.Jkcards.jk_user.repositories;

import com.Jkcards.jk_user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findById(Long id);

     boolean existsByEmail(String email);

     boolean existsByEmailAndIdNot(String email, Long id);

     Page<User> findByActiveTrue(Pageable pageable);
}
