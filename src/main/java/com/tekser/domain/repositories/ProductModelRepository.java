package com.tekser.domain.repositories;

import com.tekser.domain.business.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    ProductModel findByName(String name);

    Page<ProductModel> findByNameContainingOrderByIdAsc(String productModelName, Pageable pageable);

}
