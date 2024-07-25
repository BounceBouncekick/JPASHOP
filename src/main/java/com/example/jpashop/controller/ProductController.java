package com.example.jpashop.controller;

import com.example.jpashop.dto.ProductDto;
import com.example.jpashop.entity.Product;
import com.example.jpashop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@ResponseBody
@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto productDTO) throws IOException {
        productService.create(productDTO);
        log.info("df");
        return ResponseEntity.ok("등록되었습니다.");
    }

    //상품 목록 찾기
    @GetMapping("/List")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findItems();
        log.info("df");
        return ResponseEntity.ok(products);
    }

    //uuid조회를 통한 상품찾기
    @GetMapping("/product/{uuid}")
    public ResponseEntity<Product> findItemByuuid(@PathVariable("uuid") String uuid) {
        Product product = productService.findItemByUuid(uuid);
        log.info("df");
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    //상품수정
    @PostMapping("/update/{uuid}")
    public ResponseEntity<String> UpdatebyUuid(@PathVariable("uuid")String uuid, @ModelAttribute ProductDto productDTO){
        productService.update(uuid, productDTO);
        log.info("df");
        return ResponseEntity.ok("수정되었습니다.");
    }

    //상품삭제
    @GetMapping("/delete/{uuid}")
    public ResponseEntity<String> DeletebyUuid(@PathVariable("uuid") String uuid){
        productService.delete(uuid);
        log.info("df");
        return ResponseEntity.ok("삭제되었습니다.");
    }

    public void cool(){

    }
}
