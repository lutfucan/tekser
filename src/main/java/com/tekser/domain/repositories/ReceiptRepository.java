package com.tekser.domain.repositories;

import com.tekser.domain.business.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Page<Receipt> findByIdContainingOrderByIdAsc(Long id, Pageable pageable);

    Page<Receipt> findByClientNameOrClientSurnameContainingOrderByIdAsc(String name, String surname, Pageable pageable);

    List<Receipt> findAllByClientNameContaining(String name);

    List<Receipt> findAllByClientSurnameContaining(String name);

    Page<Receipt> findByProductNameContainingOrderByIdAsc(String product, Pageable pageable);

    Page<Receipt> findByClientIdOrderByIdAsc(Long id, Pageable pageable);

    Page<Receipt> findBySerialNumberContainingOrderByIdAsc(String serialNumber, Pageable pageable);

    Page<Receipt> findByDateOfReceiptBetweenOrderByIdAsc(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable);

    Page<Receipt> findByClientEmailContainingOrderById(String email, Pageable pageable);


    @Query("SELECT r FROM Receipt r JOIN FETCH r.client")
    List<Receipt> findAllEagerly();

    //    @Query("SELECT c FROM Client c JOIN FETCH c.receiptList WHERE c.email = (:email)")
    @Query("SELECT r FROM Receipt r JOIN FETCH r.client WHERE r.client.email = (:email)")
    Receipt findByClientEmailEagerly(@Param("email") String email);

    @Query("SELECT r FROM Receipt r JOIN FETCH r.client WHERE r.client.id = (:id)")
    Receipt findByClientIdEagerly(@Param("id") String id);

    //    @Query("SELECT c FROM Client c JOIN FETCH c.receiptList WHERE c.id = (:id)")
    @Query("SELECT r FROM Receipt r JOIN FETCH r.client WHERE r.id = (:id)")
    Receipt findByIdEagerly(@Param("id") Long id);

    @Query("SELECT r FROM Receipt r JOIN FETCH r.client WHERE r.serialNumber = (:serialNumber)")
    Receipt findBySerialNumberEagerly(@Param("serialNumber") String serialNumber);

}
