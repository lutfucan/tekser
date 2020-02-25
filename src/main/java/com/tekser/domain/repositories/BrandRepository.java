package com.tekser.domain.repositories;

import com.tekser.domain.business.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);

    Page<Brand> findByNameContainingOrderByIdAsc(String brandName, Pageable pageable);

}
