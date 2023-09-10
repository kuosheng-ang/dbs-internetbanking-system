package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.Contact;
import dbs.bankingsystem.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE LOWER(c.mobilePhone) = :mobilePhone")
    Contact findByMobilePhone(@Param("mobilePhone") String mobilePhone);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.workPhone) = :workPhone")
    Contact findByWorkPhone(@Param("workPhone") String workPhone);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.user.username) = LOWER(:username)")
    Contact findByUsername(@Param("username") String username);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.user.email) = LOWER(:email)")
    Contact findByEmail(@Param("email") String email);

    Contact save(Contact contact);
}
