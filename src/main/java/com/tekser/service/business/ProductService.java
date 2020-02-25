package com.tekser.service.business;

import com.tekser.domain.User;
import com.tekser.domain.business.Product;
import com.tekser.domain.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Product> product = productRepository.findById(id);
        List<Product> products = product.isPresent() ? Collections.singletonList(product.get()) : Collections.emptyList();
        return new PageImpl<>(products, pageRequest, products.size());
    }

    public Page<Product> findByNameContaining (String name, Pageable pageable){
        return productRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public boolean checkIfProductNameExists(List<Product> allProducts, Product product, Product persistedProduct) {
        boolean productNameAlreadyExists = false;
        for (Product product1 : allProducts) {
            //Check if the product name is edited and if it is taken
            if (!product.getName().equals(persistedProduct.getName())
                    && product.getName().equals(product1.getName())) {
                productNameAlreadyExists = true;
            }
        }
        return productNameAlreadyExists;
    }


}
