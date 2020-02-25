package com.tekser.service.business;

import com.tekser.domain.business.Accessory;
import com.tekser.domain.repositories.AccessoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class AccessoryService {
    private AccessoryRepository accessoryRepository;

    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
    }


    public List<Accessory> findAll() {
        return accessoryRepository.findAll();
    }

    public Page<Accessory> findAllPageable(Pageable pageable) {
        return accessoryRepository.findAll(pageable);
    }

    public Accessory findByName(String name) {
        return accessoryRepository.findByName(name);
    }

    public Optional<Accessory> findById(Long id) {
        return accessoryRepository.findById(id);
    }

    public Page<Accessory> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        List<Accessory> accessorys = accessory.isPresent() ? Collections.singletonList(accessory.get()) : Collections.emptyList();
        return new PageImpl<>(accessorys, pageRequest, accessorys.size());
    }

    public Page<Accessory> findByNameContaining (String name, Pageable pageable){
        return accessoryRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }

        
    @Transactional
    public void save(Accessory accessory) {
        accessoryRepository.save(accessory);
    }

    public void deleteById(Long id) {
        accessoryRepository.deleteById(id);
    }


    public boolean checkIfAccessoryNameExists(List<Accessory> accessories, Accessory accessory, Accessory persistedAccessory) {
        boolean accessoryNameAlreadyExists = false;
        for (Accessory accessory1 : accessories) {
            //Check if the accessory name is edited and if it is taken
            if (!accessory.getName().equals(persistedAccessory.getName())
                    && accessory.getName().equals(accessory1.getName())) {
                accessoryNameAlreadyExists = true;
            }
        }
        return accessoryNameAlreadyExists;
    }


}
