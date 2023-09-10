package dbs.bankingsystem.backend.repositories;


import dbs.bankingsystem.backend.entity.Address;
import dbs.bankingsystem.backend.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */


@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    @Query("SELECT addr from Address addr WHERE addr.userList IN :users")
    List<Address> findAllAddressByUsers(Collection<User> users);

    @Query("SELECT addr from Address addr WHERE addr.country like LOWER(:country)")
    Address findByAddressByCountry(@Param("country") String country);

    @Query("SELECT addr from Address addr WHERE  addr.userList IN :usernames")
    List<Address> findAllAddressByUsername(List<String> usernames);
}
