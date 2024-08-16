package com.example.jpashop.service;

import com.example.jpashop.dto.ProductDto;
import com.example.jpashop.entity.Product;
import com.example.jpashop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public void create(ProductDto productDTO) throws IOException {
        Product product = Product.toDTO(productDTO);

        log.info("product id = {}", product.getId());
        log.info("product price = {}", product.getPrice());
        log.info("product stock = {}", product.getStockQuantity());
        log.info("product boardWriter = {}", product.getBoardwriter());
        log.info("product productname = {}", product.getProductname());
        productRepository.save(product);
    }

    public List<Product> findItems(){
        return productRepository.findAll();
    }

    public Product findItemByUuid(String uuid) {
        return productRepository.findByUuid(uuid);
    }

    public void update(String uuid,ProductDto productDTO){
        Optional<Product> optionalItem = Optional.ofNullable(productRepository.findByUuid(uuid));

        if(optionalItem.isPresent()){
            Product updateProduct = optionalItem.get();
            productRepository.updateItemByUUID(uuid, productDTO.getName(),
                    productDTO.getPrice(), productDTO.getStockQuantity(),
                    productDTO.getBoardwriter(),
                    productDTO.getProductname());
        }
    }
    @Transactional
    public void delete(String uuid){
        Optional<Product> optionalItem = Optional.ofNullable(productRepository.findByUuid(uuid));

        if(optionalItem.isPresent()){
            Product product = optionalItem.get();
            productRepository.delete(product);
        }
    }
}
