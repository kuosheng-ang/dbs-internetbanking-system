package dbs.bankingsystem.backend.repositories;

import dbs.bankingsystem.backend.entity.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransferFrom(String transferFrom);

    List<Transaction> findByTransferTo(String transferTo);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountNo = :accountNumber")
    Transaction findByAccountNumber(String accountNumber);
}
