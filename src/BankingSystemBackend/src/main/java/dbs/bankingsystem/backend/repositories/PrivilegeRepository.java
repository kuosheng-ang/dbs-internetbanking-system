package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query("SELECT p FROM Privilege p WHERE LOWER(p.name) = LOWER(:privilege)")
    Privilege findByName(String privilege);

    Privilege save(Privilege privilege);
}