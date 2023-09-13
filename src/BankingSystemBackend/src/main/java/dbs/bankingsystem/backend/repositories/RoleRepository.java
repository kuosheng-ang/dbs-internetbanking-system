package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.Role;
import dbs.bankingsystem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) = LOWER(:role)")
    Role findByRole(String role);

    @Query("SELECT r FROM Role r WHERE r.users IN :users")
    List<Role> findRoleByUsers(Collection<User> users);

    Role save(Role role);
}
