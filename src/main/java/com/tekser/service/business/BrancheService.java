package com.tekser.service.business;

import com.tekser.domain.business.Branche;
import com.tekser.domain.repositories.BrancheRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BrancheService {
    private BrancheRepository brancheRepository;

    public BrancheService(BrancheRepository brancheRepository) {
        this.brancheRepository = brancheRepository;
    }


    public List<Branche> findAll() {
        return brancheRepository.findAll();
    }

    public Page<Branche> findAllPageable(Pageable pageable) {
        return brancheRepository.findAll(pageable);
    }

    public Branche findByName(String name) {
        return brancheRepository.findByName(name);
    }

    public Optional<Branche> findById(Long id) {
        return brancheRepository.findById(id);
    }

    public Page<Branche> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Branche> branche = brancheRepository.findById(id);
        List<Branche> branches = branche.isPresent() ? Collections.singletonList(branche.get()) : Collections.emptyList();
        return new PageImpl<>(branches, pageRequest, branches.size());
    }

    public Page<Branche> findByNameContaining (String name, Pageable pageable){
        return brancheRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(Branche branche) {
        brancheRepository.save(branche);
    }

    public void deleteById(Long id) {
        brancheRepository.deleteById(id);
    }


    public boolean checkIfBrancheNameExists(List<Branche> allBranches, Branche branche, Branche persistedBranche) {
        boolean brancheNameAlreadyExists = false;
        for (Branche branche1 : allBranches) {
            //Check if the branche name is edited and if it is taken
            if (!branche.getName().equals(persistedBranche.getName())
                    && branche.getName().equals(branche1.getName())) {
                brancheNameAlreadyExists = true;
            }
        }
        return brancheNameAlreadyExists;
    }


}
