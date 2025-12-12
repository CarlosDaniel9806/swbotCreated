package com.web.swbotv1.Repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.web.swbotv1.model.Admin;

public interface IAdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}