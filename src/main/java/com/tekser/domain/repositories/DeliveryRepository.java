package com.tekser.domain.repositories;

import com.tekser.domain.business.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByName(String name);

    Page<Delivery> findByNameContainingOrderByIdAsc(String deliveryName, Pageable pageable);

}
