package com.tekser.domain.repositories;

import com.tekser.domain.business.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmailAndIdNot(String email, Long id);

    Page<Client> findByPhoneContainingOrderByIdAsc(String phone, Pageable pageable);

    Page<Client> findByEmailContainingOrderByIdAsc(String email, Pageable pageable);

    Page<Client> findByNameContainingOrderByIdAsc(String name, Pageable pageable);

    Page<Client> findBySurnameContainingOrderByIdAsc(String surname, Pageable pageable);


    @Query("SELECT c FROM Client c JOIN FETCH c.receiptList")
    List<Client> findAllEagerly();

//    @Query("SELECT c FROM Client c JOIN FETCH c.receiptList WHERE c.email = (:email)")
    @Query("SELECT c FROM Client c JOIN FETCH c.clientDetail WHERE c.email = (:email)")
    Client findByEmailEagerly(@Param("email") String email);

//    @Query("SELECT c FROM Client c JOIN FETCH c.receiptList WHERE c.id = (:id)")
    @Query("SELECT c FROM Client c JOIN FETCH c.clientDetail WHERE c.id = (:id)")
    Client findByIdEagerly(@Param("id") Long id);


}
