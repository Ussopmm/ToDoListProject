package io.ussopm.AuthService.repository;

import io.ussopm.AuthService.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authority, Integer> {

    List<Authority> findAuthoritiesByRole(String role);
}
