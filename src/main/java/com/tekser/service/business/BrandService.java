package com.tekser.service.business;

import com.tekser.domain.business.Brand;
import com.tekser.domain.repositories.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BrandService {
    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }


    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Page<Brand> findAllPageable(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    public Brand findByName(String name) {
        return brandRepository.findByName(name);
    }

    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    public Page<Brand> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Brand> brand = brandRepository.findById(id);
        List<Brand> brands = brand.isPresent() ? Collections.singletonList(brand.get()) : Collections.emptyList();
        return new PageImpl<>(brands, pageRequest, brands.size());
    }

    public Page<Brand> findByNameContaining (String name, Pageable pageable){
        return brandRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }


    public boolean checkIfBrandNameExists(List<Brand> allBrands, Brand brand, Brand persistedBrand) {
        boolean brandNameAlreadyExists = false;
        for (Brand brand1 : allBrands) {
            //Check if the brand name is edited and if it is taken
            if (!brand.getName().equals(persistedBrand.getName())
                    && brand.getName().equals(brand1.getName())) {
                brandNameAlreadyExists = true;
            }
        }
        return brandNameAlreadyExists;
    }


}
