package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;




/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByAccountNo(@Param("accountNo")String accountNo);

    Account findByAccountType(@Param("accountType") String accountType);

    @Query("SELECT a from Account a WHERE a.user.username = :username ")
    Account findAccountByUsername(String username);

    @Query("SELECT a from Account a WHERE a.user.username = :username or a.user.email = :email")
    Account findAccountByUsernameOrEmail(String username, String email);
}