package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) like LOWER(:email)")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(:username)")
    User findByUsername(@Param("username") String username);

    User findUserByEmailOrUsername(@Param("email") String email, @Param("username") String username);

    @Query("SELECT u from User u WHERE u.contactDetails.mobilePhone like LOWER(:mobilePhone)")
    User findUserByMobilePhone(@Param("mobilePhone") String mobilePhone);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(:username)")
    List<User> findAllUsersByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(:email)")
    List<User> findAllUsersByEmail(@Param("email") String emails);

    @Query("select u from User u WHERE u.firstName like %?1")
    List<User> findAllUsersByFirstNameEndsWith(String name);

    @Query("select u from User u WHERE u.lastName like %?1")
    List<User> findAllUsersByLastNameEndsWith(String name);

    @Query("SELECT u FROM User u ORDER BY u.username")
    Page<User> findAllUsersWithPagination(Pageable pageable);

    @Query("SELECT u from User u WHERE u.accountDetails.accountNo = :accountNo")
    User findUserByAccountNum(String accountNo);



    @Query("SELECT u from User u WHERE u.roles IN :roles")
    List<User> findAllUsersByRoles(List<String> roles);
}
