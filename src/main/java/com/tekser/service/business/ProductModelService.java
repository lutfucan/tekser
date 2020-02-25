package com.tekser.service.business;

import com.tekser.domain.business.ProductModel;
import com.tekser.domain.repositories.ProductModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ProductModelService {
    private ProductModelRepository productModelRepository;

    public ProductModelService(ProductModelRepository productModelRepository) {
        this.productModelRepository = productModelRepository;
    }


    public List<ProductModel> findAll() {
        return productModelRepository.findAll();
    }

    public Page<ProductModel> findAllPageable(Pageable pageable) {
        return productModelRepository.findAll(pageable);
    }

    public ProductModel findByName(String name) {
        return productModelRepository.findByName(name);
    }

    public Optional<ProductModel> findById(Long id) {
        return productModelRepository.findById(id);
    }

    public Page<ProductModel> findByIdPageable(Long id, Pageable pageRequest){
        Optional<ProductModel> productModel = productModelRepository.findById(id);
        List<ProductModel> productModels = productModel.isPresent() ? Collections.singletonList(productModel.get()) : Collections.emptyList();
        return new PageImpl<>(productModels, pageRequest, productModels.size());
    }

    public Page<ProductModel> findByNameContaining (String name, Pageable pageable){
        return productModelRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(ProductModel productModel) {
        productModelRepository.save(productModel);
    }

    public void deleteById(Long id) {
        productModelRepository.deleteById(id);
    }


    public boolean checkIfProductModelNameExists(List<ProductModel> allProductModels, ProductModel productModel, ProductModel persistedProductModel) {
        boolean productModelNameAlreadyExists = false;
        for (ProductModel productModel1 : allProductModels) {
            //Check if the productModel name is edited and if it is taken
            if (!productModel.getName().equals(persistedProductModel.getName())
                    && productModel.getName().equals(productModel1.getName())) {
                productModelNameAlreadyExists = true;
            }
        }
        return productModelNameAlreadyExists;
    }


}
