package com.tekser.domain.repositories;

import com.tekser.domain.business.Branche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrancheRepository extends JpaRepository<Branche, Long> {

    Branche findByName(String name);

    Page<Branche> findByNameContainingOrderByIdAsc(String brancheName, Pageable pageable);

}
