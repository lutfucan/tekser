package com.tekser.domain.repositories;

import com.tekser.domain.business.Accessory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    Accessory findByName(String name);

    Page<Accessory> findByNameContainingOrderByIdAsc(String accessoryName, Pageable pageable);

}
