package com.xrontech.web.domain.security.repos;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUserRole(UserRole userRole);

    Optional<User> findByMobile(String mobile);
}
