package com.tekser.domain.repositories;

import com.tekser.domain.business.ClientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailRepository extends JpaRepository<ClientDetail, Long> {

    ClientDetail findByDetailName(String name);

    Page<ClientDetail> findByDetailNameContainingOrderByIdAsc(String detailName, Pageable pageable);

}
