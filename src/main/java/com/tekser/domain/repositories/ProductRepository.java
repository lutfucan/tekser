package com.tekser.domain.repositories;

import com.tekser.domain.business.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    Page<Product> findByNameContainingOrderByIdAsc(String productName, Pageable pageable);

}
